package com.jceif.data.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jceif.data.service.GenCreateOracleService;
import com.jceif.data.service.GenMysqlPoService;
import com.jceif.data.service.TableService;
import com.jceif.data.common.TableUtil;
import com.jceif.data.model.TableColumn;
import java.sql.SQLException;
import java.util.*;

@Repository("genMysqlPoService")
public class GenMysqlPoImpl implements GenMysqlPoService {

	private static final String LINE = "\r\n";
	private static final String TAB = "	";

	@Autowired
	@Qualifier("tableService")
	private TableService tableService;

	@Autowired
	@Qualifier("genCreateOracleService")
	private GenCreateOracleService genCreateOracleService;

	public String geneJavaBean(String tableName, String schemaName)
			throws Exception {

		StringBuffer sb = new StringBuffer();
		String tableNameOracle = genCreateOracleService
				.transferTableName(tableName);
		String tableNameJava = TableUtil.dealName(tableNameOracle);
		String tableNamePackage = tableNameJava.toLowerCase();

		// package
		sb.append("package " + TableUtil.MysqlPoPath + TableUtil.toClassName(tableName).toLowerCase() + " ;");
		sb.append(LINE);
		sb.append("import java.util.Date;");
		sb.append(LINE);
		sb.append("import java.io.Serializable;");
		sb.append(LINE);
		sb.append("import java.sql.Timestamp;");
		
		sb.append(LINE);
		// List<TableColumn> lstTableColumn = genCreateOracleService
		// .getOracleTableColumnInfo(tableNameJava, schemaName);

		List<TableColumn> lstTableColumn = tableService
				.getMysqlTableColumnInfo(tableName, schemaName);

		// import
		String strImport = importPackage(lstTableColumn);
		sb.append(strImport);
		sb.append(LINE);

		// 类定义

		sb.append("public class " + TableUtil.toClassName(tableName) + " implements Serializable {");

		// 属性
		sb.append(defProperty(lstTableColumn,tableName));
		sb.append(LINE);
		// get set
		sb.append(genSetGet(lstTableColumn,tableName));
		sb.append(LINE);

		sb.append(LINE);
		sb.append("}");
		return sb.toString();

	}

	/**
	 * 生成包导入数据
	 * 
	 * @param mapColumnType
	 * @return
	 * @throws SQLException
	 */
	private String importPackage(List<TableColumn> lstTableColumn)
			throws SQLException {
		String strRtn = "";
		Map hashMapColumnType = new HashMap();
		for (int i = 0; i < lstTableColumn.size(); i++) {
			TableColumn tableColumn = lstTableColumn.get(i);
			String columnName = tableColumn.getColumnName();
			String dataType = tableColumn.getDataType();

			// String columnTypeJava = getColumnType(columnName, dataType);
			String columnTypeJava = TableUtil.typeMap.get(dataType);
			System.out.println("columnName=" + columnName);
			System.out.println("dataType=" + dataType);
			System.out.println("columnTypeJava=" + columnTypeJava);
			hashMapColumnType.put(columnTypeJava, columnTypeJava);
		}
		System.out
				.println("hashMapColumnType.size=" + hashMapColumnType.size());

		Set setColumnType = hashMapColumnType.keySet();
		for (Iterator iter = setColumnType.iterator(); iter.hasNext();) {
			String strKey = (String) iter.next();
			System.out.println("hashMapColumnType.content=" + strKey);
			String impStr = TableUtil.importMap.get(strKey);
			if (impStr != null) {
				strRtn += TableUtil.importMap.get(strKey);
				strRtn += LINE;
			}

		}
		return strRtn;
	}

	/**
	 * 定义属性
	 * 
	 * @param lstTableColumn
	 * @return
	 * @throws SQLException
	 */
	private String defProperty(List<TableColumn> lstTableColumn,String tableName)
			throws SQLException {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lstTableColumn.size(); i++) {
			TableColumn tableColumn = lstTableColumn.get(i);
			String columnName = tableColumn.getColumnName();
			sb.append(LINE);
			String columnNameJava = TableUtil.dealName(columnName);
			String dataType = tableColumn.getDataType();
			
			// sb.append("private " + getColumnType(dataType, columnName)
			// + " " + columnNameJava + "; // "
			// + tableColumn.getColumnComment());
			String strJavaType = TableUtil.getJavaType(columnName, dataType,TableUtil.databaseTypeMysql,tableName);
			sb.append("private " + strJavaType + " " + columnNameJava + "; // "
					+ tableColumn.getColumnComment());

			sb.append(LINE);
		}
		return sb.toString();
	}

	/**
	 * 生成getset方法
	 * 
	 * @param lstTableColumn
	 * @throws SQLException
	 */
	private String genSetGet(List<TableColumn> lstTableColumn,String tableName)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lstTableColumn.size(); i++) {
			TableColumn tableColumn = lstTableColumn.get(i);
			String columnName = tableColumn.getColumnName();

			sb.append(LINE);
			String columnNameJava = TableUtil.dealName(columnName);
			// 数据库类型
			String dataType = tableColumn.getDataType();
			String columnNameTransfer = TableUtil.toClassName(columnName);
			// java类型

			String strJavaType = TableUtil.getJavaType(columnName, dataType,TableUtil.databaseTypeMysql,tableName);
			// String strJavaType = TableUtil.typeMap.get(dataType);

			// 生成get方法
			String strGet = "";
			strGet = "	public " + strJavaType + " get" + columnNameTransfer
					+ "() {";

			strGet += LINE;
			strGet += "		return " + "this." + columnNameJava + ";";
			strGet += LINE;
			strGet += "	}";
			// strGet += LINE;
			strGet += LINE;
			sb.append(strGet);

			// 生成set方法
			String strSet = "";
			strSet = "	public void set" + columnNameTransfer + "("
					+ strJavaType + " " + columnNameJava + ") {";
			strSet += LINE;
			strSet += "		this." + columnNameJava + " = " + columnNameJava + ";";
			strGet += LINE;
			strSet += "	}";
			strSet += LINE;
			sb.append(strSet);

		}

		return sb.toString();
	}
	

	//
	// /**
	// * 把字符串转换成java属性（首字母大写）
	// * @param columnName
	// * @return
	// */
	// private String transferColumn(String columnName) {
	// String strColumnName = TableUtil.dealName(columnName);
	//
	// if(strColumnName.length() >1){
	// strColumnName = strColumnName.substring(0, 1).toUpperCase()
	// + strColumnName.substring(1, columnName.length());
	// }else{
	// strColumnName=strColumnName.toUpperCase();
	// }
	//
	// return strColumnName;
	// }

	// // 表数据库字段转换成java属性
	// private String dealName(String name) {
	// boolean f = true;
	// name = name.toLowerCase();
	// while (f) {
	// if (name.contains("_")) {
	// int b = name.indexOf("_");
	// String temp = name.substring(b, b + 2);
	// String temp2 = name.substring(b + 1, b + 2);
	// name = name.replace(temp, temp2.toUpperCase());
	// } else {
	// f = false;
	// }
	// }
	// return name;
	// }
}
