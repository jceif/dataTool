package com.jceif.data.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jceif.data.service.GenMysqlDaoXMLService;
import com.jceif.data.service.TableService;
import com.jceif.data.common.TableUtil;
import com.jceif.data.model.TableColumn;
import java.sql.SQLException;
import java.util.List;

@Repository("genMysqlDaoXMLService")
public class GenMysqlDaoXMLImpl implements GenMysqlDaoXMLService {

	private static final String LINE = "\r\n";
	private static final String TAB = "	";

	@Autowired
	@Qualifier("tableService")
	private TableService tableService;

	public String geneMybatisXML(String tableName, String schemaName)
			throws Exception {
		List<TableColumn> tableColumns = tableService.getMysqlTableColumnInfo(
				tableName, schemaName);
		int columnCount = tableColumns.size();
		// System.out.println("columnCount=" + columnCount);
		StringBuffer sb = new StringBuffer();
		// String table_Name = tableName;
		// tableName = tableName.substring(0, 1).toUpperCase()
		// + tableName.subSequence(1, tableName.length());
		// tableName = this.dealLine(tableName);

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append(LINE);
		sb
				.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
		sb.append(LINE);
		sb.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" > ");
		sb.append(LINE);
		String className = TableUtil.toClassName(tableName);
		sb.append("<mapper namespace=\"" + TableUtil.MysqlMapperPath
				+ TableUtil.toClassName(tableName).toLowerCase() + "."
				+ className + "Mapper" + "\">");
		sb.append(LINE);
		this.genResultMap(tableColumns, columnCount, sb, tableName);
		this.genInsertSql(tableColumns, columnCount, sb, tableName);
		this.genDeleteSql(tableColumns, columnCount, sb, tableName);

		this.genDeleteAllSql(tableColumns, columnCount, sb, tableName);

		this.genSelectOneSql(tableColumns, columnCount, sb, tableName);
		this.genSelectListSql(tableColumns, columnCount, sb, tableName);
		this.genUpdateSql(tableColumns, columnCount, sb, tableName);
		// 根据ids批量删除
		this.genDeleteListByIdsSql(tableColumns, columnCount, sb, tableName);
		this.genDeleteListSql(tableColumns, columnCount, sb, tableName);
		// 批量插入
		this.genInsertListSql(tableColumns, columnCount, sb, tableName);
		// 批量更新
		this.genUpdateListSql(tableColumns, columnCount, sb, tableName);
		// 条件查询
		this.genSelectQuery(tableColumns, columnCount, sb, tableName);
		// 条件分页查询
		this.genSelectQueryPage(tableColumns, columnCount, sb, tableName);
		// 条件查询总条数
		this.genCountQuery(tableColumns, columnCount, sb, tableName);

		if (TableUtil.incrementMap.get(tableName) != null) {
			// 增量条件分页查询
			this.genSelectIncrementQueryPage(tableColumns, columnCount, sb,
					tableName);
			// 增量条件查询总条数
			this.genCountIncrementQuery(tableColumns, columnCount, sb,
					tableName);
		}

		sb.append("</mapper>");
		// String paths = System.getProperty("user.dir");
		// String endPath = paths + "\\src\\"
		// + (packages.replace("/", "\\")).replace(".", "\\");

		// String xmlName = dealLine("_" + table_Name);

		return sb.toString();
	}

	// 获得resultap 结果映射
	private void genResultMap(List<TableColumn> tableColumns, int columnCount,
			StringBuffer sb, String tableName) throws SQLException {

		String resultMapId = dealLine(tableName);
		String type = TableUtil.MysqlPoPath + dealLine(tableName).toLowerCase()
				+ "." + TableUtil.toClassName(tableName);
		sb.append("<resultMap id=\"" + resultMapId + "\" type=\"" + type
				+ "\">");
		sb.append(LINE);
		for (int i = 0; i < columnCount; i++) {
			sb.append(TAB);
			// System.out.println("tableColumns.get(" + i + ").getColumnName()"
			// + tableColumns.get(i).getColumnName());
			String columnName = tableColumns.get(i).getColumnName();

			String property = this.dealLine(columnName);

			String column = columnName;
			// if (!TableUtil.specialColumn.contains(column) ||
			// column.equals("")
			// || column.equals("CREATE_BY_ID")
			// || column.equals("CREATE_BY_NAME")
			// || column.equals("UPDATE_BY_ID")
			// || column.equals("UPDATE_BY_NAME")) {
			//				
			// }
			sb.append("<result property=\"" + property + "\" column=\""
					+ column + "\"/>");
			sb.append(LINE);
		}

		sb.append("</resultMap>");
		sb.append(LINE);
	}

	// 拼接insert
	private void genInsertSql(List<TableColumn> tableColumns, int columnCount,
			StringBuffer sb, String tableName) throws SQLException {
		String method_Name = "insert_" + tableName;
		String methodName = dealLine(method_Name);
		String parameterType = TableUtil.MysqlPoPath
				+ dealLine(tableName).toLowerCase() + "."
				+ TableUtil.toClassName(tableName);
		sb.append("<insert id=\"" + methodName + "\" parameterType=\""
				+ parameterType + "\">");
		sb.append(LINE);
		sb.append("insert into " + tableName);

		sb.append(LINE);
		sb.append("(");

		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			sb.append(TAB);
			sb.append(column + ",");
			sb.append(LINE);

		}
		TableUtil.trimAnyOne(sb, ",");
		sb.append(")");
		sb.append("values(");
		sb.append(LINE);
		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String property = this.dealLine(columnName);
			sb.append(TAB);
			sb.append("#{" + property + "}" + ",");
			sb.append(LINE);

		}
		TableUtil.trimAnyOne(sb, ",");
		sb.append(")");
		sb.append(LINE);
		sb.append("</insert>");
		sb.append(LINE);
	}

	// 拼接delete
	private void genDeleteSql(List<TableColumn> tableColumns, int columnCount,
			StringBuffer sb, String tableName) throws SQLException {
		String method_Name = "delete_" + tableName + "_By_Id";
		String methodName = dealLine(method_Name);
		sb.append("<delete id=\"" + methodName + "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("delete from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("where ");
		String columnName = tableColumns.get(0).getColumnName();
		String column = columnName;
		String property = dealLine(columnName);
		sb.append(column + "=#{" + property + "}");
		sb.append(LINE);
		sb.append("</delete>");
		sb.append(LINE);
	}

	// 拼接deleteAll
	private void genDeleteAllSql(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		String method_Name = "delete_" + tableName + "_All";
		String methodName = dealLine(method_Name);
		sb.append("<delete id=\"" + methodName + "\">");
		sb.append(LINE + TAB + "delete from " + tableName);
		sb.append(LINE + "</delete>");
		sb.append(LINE);
	}

	// 拼接根据主键查询一条
	private void genSelectOneSql(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		String method_Name = "find_" + tableName + "_By_Id";
		String methodName = dealLine(method_Name);
		String resultMap = dealLine(tableName);
		sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
				+ "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("select * from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("where ");
		String columnName = tableColumns.get(0).getColumnName();
		String column = columnName;
		String property = dealLine(columnName);
		sb.append(column + "=#{" + property + "}");
		sb.append(LINE);
		sb.append("</select>");
		sb.append(LINE);
	}

	// 拼接查询多条
	private void genSelectListSql(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		String method_Name = "find_" + tableName + "_List";
		String methodName = dealLine(method_Name);
		String resultMap = dealLine(tableName);
		sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
				+ "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("select * from " + tableName);
		sb.append(LINE);
		sb.append("</select>");
		sb.append(LINE);
	}

	// 更新sql
	private void genUpdateSql(List<TableColumn> tableColumns, int columnCount,
			StringBuffer sb, String table_name) throws SQLException {
		String method_Name = "update_" + table_name;
		String methodName = dealLine(method_Name);
		String parameterType = TableUtil.MysqlPoPath
				+ dealLine(table_name).toLowerCase() + "."
				+ TableUtil.toClassName(table_name);

		sb.append("<update id=\"" + methodName + "\" parameterType=\""
				+ parameterType + "\">");
		sb.append(LINE);
		sb.append("update " + table_name);
		sb.append(LINE);
		sb.append("<set>");
		sb.append(LINE);
		// ToDo
		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			String property = dealLine(columnName);
			sb.append(TAB);
			sb.append(column + "=#{" + property + "}" + ",");
			sb.append(LINE);
		}
		TableUtil.trimAnyOne(sb, ",");
		sb.append("</set>");
		sb.append(LINE);
		String columnName = tableColumns.get(0).getColumnName();
		String primaryKeyColumn = columnName;
		String primaryKeyProperty = dealLine(columnName);
		sb.append("<where>");
		sb.append(TAB);
		sb.append(LINE);
		sb.append(primaryKeyColumn + "=#{" + primaryKeyProperty + "}");
		sb.append(TAB);
		sb.append(LINE);
		sb.append("</where>");
		sb.append(TAB);
		sb.append(LINE);
		sb.append("</update>");
		sb.append(LINE);

	}

	// 批量删除
	private void genDeleteListSql(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {

		String method_Name = "delete_" + tableName + "_List";
		String methodName = dealLine(method_Name);
		String parameterType = TableUtil.MysqlPoPath
				+ dealLine(tableName).toLowerCase() + "."
				+ TableUtil.toClassName(tableName);
		sb.append("<delete id=\"" + methodName + "\" parameterType =\""
				+ parameterType + "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("delete from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("where ");
		String columnName = tableColumns.get(0).getColumnName();
		String column = columnName;
		String property = dealLine(columnName);
		sb.append(column + " in");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
				+ "\" index= \"" + "index" + "\" open=\"" + "("
				+ "\" separator=\"" + "," + "\" close=\"" + ")" + "\">");
		sb.append("#{item." + property + "}");
		sb.append("</foreach>");
		sb.append(LINE);
		sb.append("</delete>");
		sb.append(LINE);
	}

	// 批量删除
	private void genDeleteListByIdsSql(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		String methodName = "delete" + TableUtil.toClassName(tableName)
				+ "ListByIds";
		sb.append("<delete id=\"" + methodName + "\" parameterType =\""
				+ "java.lang.String" + "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("delete from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("where ");
		String columnName = tableColumns.get(0).getColumnName();
		String column = columnName;
		sb.append(column + " in");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<foreach item=\"" + "idItem" + "\" collection=\"" + "array"
				+ "\" open=\"" + "(" + "\" separator=\"" + "," + "\" close=\""
				+ ")" + "\">");
		sb.append("#{" + "idItem" + "}");
		sb.append("</foreach>");
		sb.append(LINE);
		sb.append("</delete>");
		sb.append(LINE);
	}

	// 批量修改
	private void genUpdateListSql(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		String method_Name = "update_" + tableName + "_List";
		String methodName = dealLine(method_Name);
		String parameterType = "java.util.List";
		sb.append("<update id=\"" + methodName + "\" parameterType=\""
				+ parameterType + "\">");
		sb.append(LINE);
		sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
				+ "\" index= \"" + "index" + "\" separator=\"" + ";" + "\" >");
		sb.append(LINE);
		sb.append("update " + tableName);
		sb.append(LINE);
		sb.append("set");
		sb.append(LINE);
		// ToDo
		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			String property = dealLine(columnName);
			sb.append(TAB);
			sb.append(column + "=#{item." + property + "}" + ",");
			sb.append(LINE);
		}
		TableUtil.trimAnyOne(sb, ",");
		sb.append(LINE);
		String columnName = tableColumns.get(0).getColumnName();
		String primaryKeyColumn = columnName;
		String primaryKeyProperty = dealLine(columnName);
		sb.append("where");
		sb.append(TAB);
		sb.append(LINE);
		sb.append(primaryKeyColumn + "=#{item." + primaryKeyProperty + "}");
		sb.append(TAB);
		sb.append(LINE);
		sb.append("</foreach>");
		sb.append(LINE);
		sb.append("</update>");
		sb.append(LINE);

	}

	// 批量添加
	private void genInsertListSql(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		String method_Name = "insert_" + tableName + "_List";
		String methodName = dealLine(method_Name);
		String parameterType = "java.util.List";
		sb.append("<insert id=\"" + methodName + "\" parameterType=\""
				+ parameterType + "\">");
		sb.append(LINE);
		sb.append("insert into " + tableName);

		sb.append(LINE);
		sb.append("(");
		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			sb.append(TAB);
			sb.append(column + ",");
			sb.append(LINE);

		}
		TableUtil.trimAnyOne(sb, ",");
		sb.append(")");
		sb.append("values");
		sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
				+ "\" index= \"" + "index" + "\" separator=\"" + "," + "\" >");
		sb.append(LINE);
		sb.append("(");
		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String property = dealLine(columnName);
			sb.append(TAB);
			sb.append("#{item." + property + "}" + ",");
			sb.append(LINE);

		}
		TableUtil.trimAnyOne(sb, ",");
		sb.append(")");
		sb.append("</foreach>");
		sb.append(LINE);
		sb.append("</insert>");
		sb.append(LINE);
	}

	// 条件分页查询
	private void genSelectQuery(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		// parameterType="java.util.Map"
		String method_Name = "find_" + tableName + "_Query";
		String methodName = dealLine(method_Name);
		// String resultMap = dealLine(tableName).toLowerCase();
		String resultMap = dealLine(tableName);
		sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
				+ "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("select * from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<where>");
		sb.append(LINE);

		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			String property = dealLine(columnName);
			// if (!TableUtil.specialColumn.contains(column) ||
			// column.equals("")
			// || column.equals("CREATE_BY_ID")
			// || column.equals("CREATE_BY_NAME")
			// || column.equals("UPDATE_BY_ID")
			// || column.equals("UPDATE_BY_NAME")) {
			// sb.append(TAB);
			// sb.append(" <if test=\"" + property + "!=null and " + property
			// + "!=''\"> ");
			// sb.append(LINE);
			// sb.append(TAB);
			// sb.append(TAB);
			// sb.append("AND " + column + "=#{" + property + "}");
			// sb.append(LINE);
			// sb.append(TAB);
			// sb.append("</if>");
			// sb.append(LINE);
			// }
			sb.append(TAB);
			sb.append(" <if test=\"" + property + "!=null and " + property
					+ "!=''\"> ");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("AND " + column + "=#{" + property + "}");
			sb.append(LINE);
			sb.append(TAB);
			sb.append("</if>");
			sb.append(LINE);
		}
		sb.append("</where>");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("</select>");
		sb.append(LINE);
	}

	// 条件分页查询
	private void genSelectQueryPage(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		// parameterType="java.util.Map"
		String method_Name = "find_" + tableName + "_Query_Page";
		String methodName = dealLine(method_Name);
		String resultMap = dealLine(tableName);
		sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
				+ "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("select * from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<where>");
		sb.append(LINE);

		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			String property = dealLine(columnName);
			// <if test="belongCallerId !=null and belongCallerId!=''">
			// AND BELONG_CALLER_ID = #{belongCallerId}
			// </if>
			// if (!TableUtil.specialColumn.contains(column) ||
			// column.equals("")
			// || column.equals("CREATE_BY_ID")
			// || column.equals("CREATE_BY_NAME")
			// || column.equals("UPDATE_BY_ID")
			// || column.equals("UPDATE_BY_NAME")) {
			//
			// sb.append(TAB);
			// sb.append(" <if test=\"" + property + "!=null and " + property
			// + "!=''\"> ");
			// sb.append(LINE);
			// sb.append(TAB);
			// sb.append(TAB);
			// sb.append("AND " + column + "=#{" + property + "}");
			// sb.append(LINE);
			// sb.append(TAB);
			// sb.append("</if>");
			// sb.append(LINE);
			// }
			sb.append(TAB);
			sb.append(" <if test=\"" + property + "!=null and " + property
					+ "!=''\"> ");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("AND " + column + "=#{" + property + "}");
			sb.append(LINE);
			sb.append(TAB);
			sb.append("</if>");
			sb.append(LINE);
		}

		sb.append("</where>");
		// <if
		// test="startIndex !=null and startIndex!=''and limit !=null and limit!=''">
		// limit #{startIndex},#{limit}
		// </if>
		sb.append(LINE);
		sb.append(TAB);
		// sb.append(" <if test=\"" + "startIndex"
		// + "!=null and startIndex!='' and " + "limit" + "!=null and "
		// + "limit" + "!=''\"> ");
		// sb.append(LINE);
		// sb.append(TAB);
		sb.append(TAB);
		sb.append("limit #{startIndex},#{limit}");
		sb.append(LINE);
		// sb.append(TAB);
		// sb.append("</if>");
		sb.append(LINE);
		sb.append("</select>");
		sb.append(LINE);
	}

	// 条件查询总条数
	private void genCountQuery(List<TableColumn> tableColumns, int columnCount,
			StringBuffer sb, String tableName) throws SQLException {
		// findFnUserById
		// findFnUserList

		// /parameterType="java.util.Map"
		String method_Name = "find_Count_" + tableName + "_Query";
		String methodName = dealLine(method_Name);
		// String resultMap = dealLine(tableName).toLowerCase();

		String resultMap = "java.lang.Integer";
		sb.append("<select id=\"" + methodName + "\" resultType=\"" + resultMap
				+ "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("select count(*) from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<where>");
		sb.append(LINE);

		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			String property = dealLine(columnName);
			// <if test="belongCallerId !=null and belongCallerId!=''">
			// AND BELONG_CALLER_ID = #{belongCallerId}
			// </if>
			// if (!TableUtil.specialColumn.contains(column) ||
			// column.equals("")
			// || column.equals("CREATE_BY_ID")
			// || column.equals("CREATE_BY_NAME")
			// || column.equals("UPDATE_BY_ID")
			// || column.equals("UPDATE_BY_NAME")) {
			//
			// sb.append(TAB);
			// sb.append(" <if test=\"" + property + "!=null and " + property
			// + "!=''\"> ");
			// sb.append(LINE);
			// sb.append(TAB);
			// sb.append(TAB);
			// sb.append("AND " + column + "=#{" + property + "}");
			// sb.append(LINE);
			// sb.append(TAB);
			// sb.append("</if>");
			// sb.append(LINE);
			// }
			sb.append(TAB);
			sb.append(" <if test=\"" + property + "!=null and " + property
					+ "!=''\"> ");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("AND " + column + "=#{" + property + "}");
			sb.append(LINE);
			sb.append(TAB);
			sb.append("</if>");
			sb.append(LINE);

		}
		sb.append("</where>");
		sb.append(LINE);
		sb.append("</select>");
		sb.append(LINE);
	}

	// 增量条件分页查询
	private void genSelectIncrementQueryPage(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		// parameterType="java.util.Map"
		String method_Name = "find_" + tableName + "_Increment_Query_Page";
		String methodName = dealLine(method_Name);
		String resultMap = dealLine(tableName);
		sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
				+ "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("select * from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<where>");
		sb.append(LINE);

		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			String property = dealLine(columnName);
			// <if test="belongCallerId !=null and belongCallerId!=''">
			// AND BELONG_CALLER_ID = #{belongCallerId}
			// </if>
//			if (!TableUtil.specialColumn.contains(column) || column.equals("")
//					|| column.equals("CREATE_BY_ID")
//					|| column.equals("CREATE_BY_NAME")
//					|| column.equals("UPDATE_BY_ID")
//					|| column.equals("UPDATE_BY_NAME")) {
//
//				sb.append(TAB);
//				sb.append(" <if test=\"" + property + "!=null and " + property
//						+ "!=''\"> ");
//				sb.append(LINE);
//				sb.append(TAB);
//				sb.append(TAB);
//				sb.append("AND " + column + "=#{" + property + "}");
//				sb.append(LINE);
//				sb.append(TAB);
//				sb.append("</if>");
//				sb.append(LINE);
//			}
			sb.append(TAB);
			sb.append(" <if test=\"" + property + "!=null and " + property
					+ "!=''\"> ");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("AND " + column + "=#{" + property + "}");
			sb.append(LINE);
			sb.append(TAB);
			sb.append("</if>");
			sb.append(LINE);
		}
		String incrementColumn = TableUtil.incrementMap.get(tableName);
		// AND
		// DATE_FORMAT(DATE_ADD('1970-01-01 08:00:00', interval
		// floor(create_time/1000) second),'%Y-%m-%d') >
		// DATE_FORMAT(SUBDATE(now(),interval 2 day),'%Y-%m-%d')
		sb.append("AND");
		sb.append(LINE
				+ "DATE_FORMAT(DATE_ADD('1970-01-01 08:00:00', interval floor("
				+ incrementColumn + "/1000) second),'%Y-%m-%d') >");
		sb.append(LINE
				+ "DATE_FORMAT(SUBDATE(now(),interval 2 day),'%Y-%m-%d')");
		sb.append("</where>");
		sb.append(LINE);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("limit #{startIndex},#{limit}");
		sb.append(LINE);
		// sb.append(TAB);
		// sb.append("</if>");
		sb.append(LINE);
		sb.append("</select>");
		sb.append(LINE);
	}

	// 增量条件查询总条数
	private void genCountIncrementQuery(List<TableColumn> tableColumns,
			int columnCount, StringBuffer sb, String tableName)
			throws SQLException {
		// findFnUserById
		// findFnUserList

		// /parameterType="java.util.Map"
		String method_Name = "find_Count_" + tableName + "_Increment_Query";
		String methodName = dealLine(method_Name);
		// String resultMap = dealLine(tableName).toLowerCase();

		String resultMap = "java.lang.Integer";
		sb.append("<select id=\"" + methodName + "\" resultType=\"" + resultMap
				+ "\">");
		sb.append(LINE);
		sb.append(TAB);
		sb.append("select count(*) from " + tableName);
		sb.append(LINE);
		sb.append(TAB);
		sb.append("<where>");
		sb.append(LINE);

		for (int i = 0; i < columnCount; i++) {
			String columnName = tableColumns.get(i).getColumnName();
			String column = columnName;
			String property = dealLine(columnName);
			// <if test="belongCallerId !=null and belongCallerId!=''">
			// AND BELONG_CALLER_ID = #{belongCallerId}
			// </if>
//			if (!TableUtil.specialColumn.contains(column) || column.equals("")
//					|| column.equals("CREATE_BY_ID")
//					|| column.equals("CREATE_BY_NAME")
//					|| column.equals("UPDATE_BY_ID")
//					|| column.equals("UPDATE_BY_NAME")) {
//
//				sb.append(TAB);
//				sb.append(" <if test=\"" + property + "!=null and " + property
//						+ "!=''\"> ");
//				sb.append(LINE);
//				sb.append(TAB);
//				sb.append(TAB);
//				sb.append("AND " + column + "=#{" + property + "}");
//				sb.append(LINE);
//				sb.append(TAB);
//				sb.append("</if>");
//				sb.append(LINE);
//			}
			sb.append(TAB);
			sb.append(" <if test=\"" + property + "!=null and " + property
					+ "!=''\"> ");
			sb.append(LINE);
			sb.append(TAB);
			sb.append(TAB);
			sb.append("AND " + column + "=#{" + property + "}");
			sb.append(LINE);
			sb.append(TAB);
			sb.append("</if>");
			sb.append(LINE);
		}
		String incrementColumn = TableUtil.incrementMap.get(tableName);
		// AND
		// DATE_FORMAT(DATE_ADD('1970-01-01 08:00:00', interval
		// floor(create_time/1000) second),'%Y-%m-%d') >
		// DATE_FORMAT(SUBDATE(now(),interval 2 day),'%Y-%m-%d')
		sb.append("AND");
		sb.append(LINE
				+ "DATE_FORMAT(DATE_ADD('1970-01-01 08:00:00', interval floor("
				+ incrementColumn + "/1000) second),'%Y-%m-%d') >");
		sb.append(LINE
				+ "DATE_FORMAT(SUBDATE(now(),interval 2 day),'%Y-%m-%d')");
		sb.append("</where>");
		sb.append(LINE);
		sb.append("</select>");
		sb.append(LINE);
	}

	private String dealLine(String tableName) {
		tableName = TableUtil.dealName(tableName);
		return tableName;
	}

}
