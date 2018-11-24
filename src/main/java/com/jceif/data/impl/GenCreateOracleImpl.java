package com.jceif.data.impl;


import com.jceif.data.model.Table;
import com.jceif.data.model.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jceif.data.service.GenCreateOracleService;
import com.jceif.data.service.TableService;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成oracle表的创建语句
 * 
 * @author Administrator
 * 
 */
@Repository("genCreateOracleService")
public class GenCreateOracleImpl implements GenCreateOracleService {

	@Autowired
	@Qualifier("tableService")
	private TableService tableService;

	/**
	 * 获得oracle表的信息
	 */
	public List<TableColumn> getOracleTableColumnInfo(String tableName,
			String schemaName) {
		initRevservedKeyList();
		List<TableColumn> lstTableColumn = tableService.getMysqlTableColumnInfo(tableName, schemaName);
		for (int i = 0; i < lstTableColumn.size(); i++) {
			TableColumn tableColumn = lstTableColumn.get(i);
			String strColumnName = tableColumn.getColumnName();

			// SIZE 是关键字，不能作为列名
			for (int j = 0; j < lstReservedKey.size(); j++) {
				String strReservedKey = lstReservedKey.get(j);
				if (strReservedKey.equalsIgnoreCase(strColumnName)) {
					strColumnName = "" + strReservedKey + "_ENTITY";
				}
			}

			tableColumn.setColumnName(strColumnName);
		}

		return lstTableColumn;
	}

	/**
	 * 生成表的插入语句
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public String generateInsert(String tableName, String schemaName) {

		List<TableColumn> lstTableColumn = getOracleTableColumnInfo(tableName,
				schemaName);

		int inListsize = lstTableColumn.size();

		// 生成插入 和选择主体
		String strInsert = "";
		String strValue = "";
		for (int i = 0; i < inListsize; i++) {
			TableColumn tableColumn = lstTableColumn.get(i);
			String columnName = tableColumn.getColumnName().toUpperCase();
			String dataType = tableColumn.getDataType().toLowerCase();
			long datalength = tableColumn.getCharacterOctetLength();

			if (!"".equals(strInsert)) {
				strInsert += ",";
			}
			strInsert += columnName;

			if (!"".equals(strValue)) {
				strValue += ",";
			}

			if (isDateColumn(tableName, columnName, dataType)) {
				strValue += " (to_date('1970-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss') + ("
						+ columnName + "+8*60*60*1000)/1000/24/60/60) ";
			} else {
				strValue += columnName;
			}
		}
		// 表名
		Table table = tableService.getTableDefine(tableName, schemaName);
		String strTableName = transferTableName(table.getTableName());

		String strReturn = "";

		// 生成完整插入语句
		strReturn += "INSERT INTO " + strTableName + " (" + strInsert + ") ";

		strReturn += " SELECT ";

		strReturn += strValue;

		strReturn += " FROM testcreate." + strTableName + ";";
		strReturn += "\n";

		strReturn += "commit;";
		strReturn += "\n";
		return strReturn;

	}

	/**
	 * 生成创建表语句
	 * 
	 * @param tableName
	 * @return
	 */
	private String generateCreateTable(String tableName, String schemaName,
			boolean specialFlag) {
		String strTableName = transferTableName(tableName);
		String strTmp = "";
		// 删除约束及表定义
		strTmp += "alter table " + strTableName + " drop primary key cascade;";
		strTmp += "\n";

		List<TableColumn> lstTableColumn = getOracleTableColumnInfo(tableName,
				schemaName);

		strTmp += "\n";

		strTmp += " drop table " + strTableName + " cascade constraints; ";
		strTmp += "\n";

		// 创建表
		strTmp += " create table " + strTableName + "  (";
		strTmp += "\n";
		strTmp += generateColumnDefineAll(tableName, lstTableColumn,
				specialFlag);
		strTmp += " );";
		strTmp += "\n";

		strTmp += generateComments(tableName, schemaName);

		// 创建主键
		strTmp += createPrimaryKey(tableName, schemaName);

		return strTmp;

	}

	/**
	 * 生成创建表语句
	 * 
	 * @param tableName
	 * @return
	 */
	public String generateCreateTableSpecial(String tableName, String schemaName) {
		String strTmp = generateCreateTable(tableName, schemaName, true);

		return strTmp;

	}

	/**
	 * 生成创建表语句
	 * 
	 * @param tableName
	 * @return
	 */
	public String generateCreateTableNospecial(String tableName,
			String schemaName) {
		String strTmp = generateCreateTable(tableName, schemaName, false);

		return strTmp;

	}

	/**
	 * 生成注释
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public String generateComments(String tableName, String schemaName) {
		String strTmp = "";

		// 生成表注释
		Table table = tableService.getTableDefine(tableName, schemaName);
		String strTableName = transferTableName(table.getTableName());

		strTmp = "comment on table " + strTableName + " is '"
				+ table.getTableComment() + "';";
		strTmp += "\n";

		String strReturn = "";
		strReturn += strTmp;

		List<TableColumn> lstTableColumn = getOracleTableColumnInfo(tableName,
				schemaName);
		int inListsize = lstTableColumn.size();

		// 生成列注释
		for (int i = 0; i < inListsize; i++) {
			strTmp = "";

			TableColumn tableColumn = lstTableColumn.get(i);
			String columnComment = tableColumn.getColumnComment();
			String columnName = tableColumn.getColumnName().toUpperCase();
			strTmp = "comment on column " + strTableName + "." + columnName
					+ " is '" + columnComment + "';";
			strTmp += "\n";
			strReturn += strTmp;
		}

		return strReturn;

	}

	/**
	 * 
	 * 生成所有列的定义信息
	 * 
	 * @param lstTableColumn
	 *            列集合
	 * @return
	 */
	private String generateColumnDefineAll(String tableName,
										   List<TableColumn> lstTableColumn, boolean specialFlag) {

		int inListsize = lstTableColumn.size();

		String strReturn = "";

		for (int i = 0; i < inListsize; i++) {
			String strTmp = "";

			TableColumn tableColumn = lstTableColumn.get(i);
			String dataType = tableColumn.getDataType().toUpperCase();
			String columnName = tableColumn.getColumnName().toUpperCase();
			long datalength = tableColumn.getCharacterOctetLength();

			int prec = tableColumn.getNumericPrecision()
					+ tableColumn.getNumericScale();
			if (prec > 22) {
				prec = 22;
			}
			int scale = tableColumn.getNumericScale();
			long characterOctetLength = tableColumn.getCharacterOctetLength();

			if (characterOctetLength > 4000) {
				characterOctetLength = 4000;
			}

			// 整型
			if ("TINYINT".equals(dataType) || "SMALLINT".equals(dataType)
					|| "MEDIUMINT".equals(dataType) || "INT".equals(dataType)) {
				strTmp = columnName + "   NUMBER(11,0)";
			} else if ("BIGINT".equals(dataType)) {
				strTmp = columnName + "   NUMBER(22,0)";
			} else if ("DECIMAL".equals(dataType) || "FLOAT".equals(dataType)
					|| "DOUBLE".equals(dataType)) {
				strTmp = columnName + "   NUMBER(" + prec + "," + scale + ")";
			} else if ("DATE".equals(dataType) || "DATETIME".equals(dataType)
					|| "TIME".equals(dataType) || "YEAR".equals(dataType)) {
				strTmp = columnName + "   DATE";
			} else if ("TIMESTAMP".equals(dataType)) {
				strTmp = columnName + "   TIMESTAMP";
			} else if ("CHAR".equals(dataType)) {

				if (characterOctetLength >= 4000) {
					strTmp = columnName + "   CLOB";
				} else {
					strTmp = columnName + "   CHAR(" + characterOctetLength
							+ ")";
				}

			} else if ("VARCHAR".equals(dataType)) {
				if (characterOctetLength >= 4000) {
					strTmp = columnName + "   CLOB";
				} else {
					strTmp = columnName + "   VARCHAR2(" + characterOctetLength
							+ ")";
				}
			} else if ("TEXT".equals(dataType) || "TINYTEXT".equals(dataType)
					|| "MEDIUMTEXT".equals(dataType)
					|| "LONGTEXT".equals(dataType)) {
				strTmp = columnName + "   CLOB";
			} else if ("TINYBLOB".equals(dataType) || "BLOB".equals(dataType)
					|| "MEDIUMBLOB".equals(dataType)
					|| "LONGBLOB".equals(dataType)) {
				strTmp = columnName + "   BLOB";
			} else if ("BOOL".equals(dataType) || "BOOLEAN".equals(dataType)) {
				strTmp = columnName + "   NUMBER(1, 0)";
			} else if ("ENUM".equals(dataType) || "SET".equals(dataType)) {
				strTmp = columnName + "   VARCHAR2(2000)";
			}
			if ("PRI".equals(tableColumn.getColumnKey())) {
				strTmp += " NOT NULL ";
			}

			if (specialFlag) {
				if (isDateColumn(tableName, columnName, dataType)) {
					strTmp = columnName + "   DATE";
				}
			}

			// 不是最后一列，追加逗号
			if (i < (inListsize - 1)) {
				strTmp += " ,";
			}

			strTmp += "\n";
			strTmp = "   " + strTmp;

			strReturn += strTmp;
		}

		return strReturn;

	}

	/**
	 * 判断列是否需要转换成日期型
	 * 
	 * @param columnName
	 * @return
	 */
	private boolean isDateColumn(String tableName, String columnName,
			String dataType) {
		boolean bolDateCol = false;
		String strCompare = columnName.toUpperCase();

		if (strCompare.indexOf("_TIME") > -1) {
			bolDateCol = true;
		}

		if ("INCOME".equalsIgnoreCase(tableName)
				&& "DEADLINE".equalsIgnoreCase(columnName)) {
			bolDateCol = true;
		}

		if ("CREDIT_ASSIGNMENT".equalsIgnoreCase(tableName)
				&& ("RELEASE_DEADLINE".equalsIgnoreCase(columnName) || "NEXT_REPAYMENT_DEADLINE"
						.equalsIgnoreCase(columnName))) {
			bolDateCol = true;
		}
		if ("DATE".equalsIgnoreCase(dataType)
				|| "DATETIME".equalsIgnoreCase(dataType)
				|| "TIME".equalsIgnoreCase(dataType)
				|| "YEAR".equalsIgnoreCase(dataType)) {
			bolDateCol = false;
		}
		return bolDateCol;
	}



	// oracle保留字不能作为列名，前面加ENTITY前缀
	private static List<String> lstReservedKey = null;

	private void initRevservedKeyList() {
		if (null == lstReservedKey) {
			lstReservedKey = new ArrayList<String>();
			lstReservedKey.add("LOCK");
			lstReservedKey.add("NOWAIT");
			lstReservedKey.add("+");
			lstReservedKey.add("RAW");
			lstReservedKey.add(")");
			lstReservedKey.add("EXISTS");
			lstReservedKey.add("INSERT");
			lstReservedKey.add("DROP");
			lstReservedKey.add("BETWEEN");
			lstReservedKey.add("FROM");
			lstReservedKey.add("DESC");
			lstReservedKey.add("OPTION");
			lstReservedKey.add("PRIOR");
			lstReservedKey.add("LONG");
			lstReservedKey.add("THEN");
			lstReservedKey.add("DEFAULT");
			lstReservedKey.add("IS");
			lstReservedKey.add(",");
			lstReservedKey.add("AS");
			lstReservedKey.add("=");
			lstReservedKey.add("INTO");
			lstReservedKey.add("INTEGER");
			lstReservedKey.add("UPDATE");
			lstReservedKey.add("GRANT");
			lstReservedKey.add("/");
			lstReservedKey.add("CHAR");
			lstReservedKey.add("&");
			lstReservedKey.add("FLOAT");
			lstReservedKey.add("ALL");
			lstReservedKey.add("^");
			lstReservedKey.add("ORDER");
			lstReservedKey.add("DATE");
			lstReservedKey.add("IN");
			lstReservedKey.add("(");
			lstReservedKey.add("RESOURCE");
			lstReservedKey.add("TABLE");
			lstReservedKey.add("@");
			lstReservedKey.add("EXCLUSIVE");
			lstReservedKey.add("VALUES");
			lstReservedKey.add("RENAME");
			lstReservedKey.add("DECIMAL");
			lstReservedKey.add("-");
			lstReservedKey.add("ALTER");
			lstReservedKey.add("INDEX");
			lstReservedKey.add("FOR");
			lstReservedKey.add("WHERE");
			lstReservedKey.add("CHECK");
			lstReservedKey.add("SMALLINT");
			lstReservedKey.add("WITH");
			lstReservedKey.add("DELETE");
			lstReservedKey.add("REVOKE");
			lstReservedKey.add("PCTFREE");
			lstReservedKey.add("PUBLIC");
			lstReservedKey.add("SIZE");
			lstReservedKey.add("<");
			lstReservedKey.add("|");
			lstReservedKey.add("ELSE");
			lstReservedKey.add("NUMBER");
			lstReservedKey.add("NOCOMPRESS");
			lstReservedKey.add(">");
			lstReservedKey.add("SHARE");
			lstReservedKey.add("NULL");
			lstReservedKey.add("GROUP");
			lstReservedKey.add("ASC");
			lstReservedKey.add("ON");
			lstReservedKey.add(":");
			lstReservedKey.add("!");
			lstReservedKey.add("VIEW");
			lstReservedKey.add("SET");
			lstReservedKey.add("COMPRESS");
			lstReservedKey.add("CLUSTER");
			lstReservedKey.add("[");
			lstReservedKey.add("VARCHAR2");
			lstReservedKey.add("NOT");
			lstReservedKey.add("LIKE");
			lstReservedKey.add("TRIGGER");
			lstReservedKey.add("SELECT");
			lstReservedKey.add("CREATE");
			lstReservedKey.add("INTERSECT");
			lstReservedKey.add("]");
			lstReservedKey.add("SYNONYM");
			lstReservedKey.add("*");
			lstReservedKey.add("DISTINCT");
			lstReservedKey.add("CONNECT");
			lstReservedKey.add(".");
			lstReservedKey.add("MODE");
			lstReservedKey.add("OF");
			lstReservedKey.add("BY");
			lstReservedKey.add("UNIQUE");
			lstReservedKey.add("HAVING");
			lstReservedKey.add("VARCHAR");
			lstReservedKey.add("ANY");
			lstReservedKey.add("UNION");
			lstReservedKey.add("TO");
			lstReservedKey.add("OR");
			lstReservedKey.add("START");
			lstReservedKey.add("IDENTIFIED");
			lstReservedKey.add("AND");
			lstReservedKey.add("MINUS");

			lstReservedKey.add("USER");
			lstReservedKey.add("UID");
			lstReservedKey.add("LEVEL");

		}
	}

	/**
	 * 取得主键列，以逗号分隔
	 * 
	 * @param
	 * @return
	 */
	private String createPrimaryKey(String tableName, String schemaName) {

		String strTmp = "";
		String strTableName = transferTableName(tableName);

		List<TableColumn> lstTableColumn = getOracleTableColumnInfo(tableName,
				schemaName);
		int cntPri = 0;
		for (int i = 0; i < lstTableColumn.size(); i++) {

			TableColumn tableColumn = lstTableColumn.get(i);

			if ("PRI".equals(tableColumn.getColumnKey())) {
				cntPri++;
				if (!"".equals(strTmp)) {
					strTmp += " , ";
				}
				strTmp += tableColumn.getColumnName().toUpperCase();
			}

		}

		String strRtn = "";

		// 存在主键
		if (cntPri > 0) {
			int inLenTable = strTableName.length();
			if (inLenTable > 27) {
				inLenTable = 27;
			}
			strRtn = "alter table " + strTableName + "	add constraint PK_"
					+ strTableName.substring(0, inLenTable) + " primary key ("
					+ strTmp + ");";
			strRtn += "\n";
		}
		return strRtn;
	}

	/**
	 * 有些关键字不能作为表名
	 * 
	 * @param tableName
	 * @return
	 */
	public String transferTableName(String tableName) {
		String strTableName = tableName;
		if ("USER".equalsIgnoreCase(tableName)) {
			strTableName = tableName + "_ENTITY";
		}
		int inTableLen = strTableName.length();
		if (inTableLen > 30) {
			inTableLen = 30;
		}
		return strTableName.toUpperCase().substring(0, inTableLen);
	}
}
