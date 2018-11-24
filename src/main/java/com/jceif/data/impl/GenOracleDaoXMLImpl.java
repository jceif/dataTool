package com.jceif.data.impl;

import com.jceif.data.model.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jceif.data.service.GenCreateOracleService;
import com.jceif.data.service.GenOracleDaoXMLService;
import com.jceif.data.common.TableUtil;

import java.sql.SQLException;
import java.util.List;

@Repository("genOracleDaoXMLService")
public class GenOracleDaoXMLImpl implements GenOracleDaoXMLService {

    private static final String LINE = "\r\n";
    private static final String TAB = "	";

    // @Autowired
    // @Qualifier("tableService")
    // private TableService tableService;

    @Autowired
    @Qualifier("genCreateOracleService")
    private GenCreateOracleService genCreateOracleService;

    public String geneMybatisXML(String tableName, String schemaName)
            throws Exception {
        // TODO Auto-generated method stub
        List<TableColumn> tableColumns = genCreateOracleService
                .getOracleTableColumnInfo(tableName, schemaName);
        int columnCount = tableColumns.size();

        //
        System.out.println("columnCount=" + columnCount);
        //

        StringBuffer sb = new StringBuffer();

        // tableName = tableName.substring(0, 1).toUpperCase()
        // + tableName.subSequence(1, tableName.length());
        // tableName = this.dealLine(tableName);

        String className = TableUtil.toOracleClassName(tableName);

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append(LINE);
        sb
                .append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
        sb.append(LINE);
        sb.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" > ");
        sb.append(LINE);
        System.out.println("className=" + className);
        sb.append("<mapper namespace=\"" + TableUtil.OracleMapperPath
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

        String className = TableUtil.toOracleClassName(tableName);
        String resultMapId = TableUtil.toParameterName(className);
        String type = TableUtil.OraclePoPath
                + dealLine(tableName).toLowerCase() + "."
                + TableUtil.toOracleClassName(tableName);
        sb.append("<resultMap id=\"" + resultMapId + "\" type=\"" + type
                + "\">");
        sb.append(LINE);
        for (int i = 0; i < columnCount; i++) {
            sb.append(TAB);
            System.out.println("tableColumns.get(" + i + ").getColumnName()"
                    + tableColumns.get(i).getColumnName());
            String columnName = tableColumns.get(i).getColumnName();
            String dataType = tableColumns.get(i).getDataType();

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
            // 数据类型为text时resultMap需要转化
            if ("TEXT".equals(dataType) || "TINYTEXT".equals(dataType)
                    || "MEDIUMTEXT".equals(dataType)
                    || "LONGTEXT".equals(dataType) || "text".equals(dataType)
                    || "tinytext".equals(dataType)
                    || "mediumtext".equals(dataType)
                    || "longtext".equals(dataType)) {

                sb
                        .append("<result property=\""
                                + property
                                + "\" column=\""
                                + column
                                + "\" jdbcType=\"CLOB\" javaType = \"java.lang.String\"  typeHandler =\"com.minxindai.dc.handler.HandlerClob\"/>");

            } else {
                sb.append("<result property=\"" + property + "\" column=\""
                        + column + "\"/>");
            }

            sb.append(LINE);
        }

        sb.append("</resultMap>");
        sb.append(LINE);
    }

    // 拼接insert
    private void genInsertSql(List<TableColumn> tableColumns, int columnCount,
                              StringBuffer sb, String tableName) throws SQLException {

        String methodName = "insert" + TableUtil.toOracleClassName(tableName);
        String parameterType = TableUtil.OraclePoPath
                + dealLine(tableName).toLowerCase() + "."
                + TableUtil.toOracleClassName(tableName);
        sb.append("<insert id=\"" + methodName + "\" parameterType=\""
                + parameterType + "\">");
        sb.append(LINE);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("insert into " + oracleTableName);

        sb.append(LINE);
        sb.append("(");

        for (int i = 0; i < columnCount; i++) {
            String columnName = tableColumns.get(i).getColumnName();
            String column = columnName;
            // String dataType = tableColumns.get(i).getDataType();
            // System.out.println("tableColumns.get("+i+").getDataType()= "+dataType);
            // if (!TableUtil.specialColumn.contains(column) ||
            // column.equals("")
            // || column.equals("CREATE_BY_ID")
            // || column.equals("CREATE_BY_NAME")) {
            // sb.append(TAB);
            // sb.append(column + ",");
            // sb.append(LINE);
            // } else if (column.equals("CREATE_TIME")) {
            // sb.append(TAB);
            // sb.append(column + ",");
            // sb.append(LINE);
            // }
            sb.append(TAB);
            sb.append(column + ",");
            sb.append(LINE);

        }
        sb.deleteCharAt(sb.length() - 3);
        sb.append(")");
        sb.append("values(");
        sb.append(LINE);
        for (int i = 0; i < columnCount; i++) {
            String columnName = tableColumns.get(i).getColumnName();
            String property = this.dealLine(columnName);
            String dataType = tableColumns.get(i).getDataType();
            sb.append(TAB);
            sb.append("#{" + property + "},");
            sb.append(LINE);
            // if (!TableUtil.specialColumn.contains(property)
            // || property.equals("createById")
            // || property.equals("createByName")) {
            // sb.append(TAB);
            // // ***数据类型为text时resultMap需要转化***
            // if ("TEXT".equals(dataType) || "TINYTEXT".equals(dataType)
            // || "MEDIUMTEXT".equals(dataType)
            // || "LONGTEXT".equals(dataType)) {
            // // sb.append("#{" + property + ",jdbcType=" + "CLOB" + "}"
            // // + ",");
            // sb.append("#{" + property + "},");
            //
            // }
            //
            // // sb.append("#{" + property +",jdbcType="+ dataType+ "}" +
            // // ",");
            // sb.append("#{" + property + "},");
            // sb.append(LINE);
            //
            // } else if (property.equals("createTime")) {
            // sb.append(TAB);
            //
            // // sb.append("now()" + ",jdbcType="+ dataType +",");
            // sb.append("sysdate,");
            // sb.append(LINE);
            // }

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
        String methodName = "delete" + TableUtil.toOracleClassName(tableName)
                + "ById";
        sb.append("<delete id=\"" + methodName + "\">");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("delete from " + oracleTableName);
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
        String methodName = "delete" + TableUtil.toOracleClassName(tableName)
                + "All";
        sb.append("<delete id=\"" + methodName + "\">");
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append(LINE + TAB + "delete from " + oracleTableName);
        sb.append(LINE + "</delete>");
        sb.append(LINE);
    }

    // 拼接根据主键查询一条
    private void genSelectOneSql(List<TableColumn> tableColumns,
                                 int columnCount, StringBuffer sb, String tableName)
            throws SQLException {
        // String method_Name = "find_" + tableName + "_By_Id";
        // String methodName = dealLine(method_Name);
        // String resultMap = dealLine(tableName);

        String className = TableUtil.toOracleClassName(tableName);
        String resultMap = TableUtil.toParameterName(className);
        String methodName = "find" + TableUtil.toOracleClassName(tableName)
                + "ById";

        sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
                + "\">");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("select * from " + oracleTableName);
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
        String className = TableUtil.toOracleClassName(tableName);
        String resultMap = TableUtil.toParameterName(className);
        String methodName = "find" + TableUtil.toOracleClassName(tableName)
                + "List";

        sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
                + "\">");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("select * from " + oracleTableName);
        sb.append(LINE);
        sb.append("</select>");
        sb.append(LINE);
    }

    // 更新sql
    private void genUpdateSql(List<TableColumn> tableColumns, int columnCount,
                              StringBuffer sb, String tableName) throws SQLException {

        String methodName = "update" + TableUtil.toOracleClassName(tableName);
        String parameterType = TableUtil.OraclePoPath
                + dealLine(tableName).toLowerCase() + "."
                + TableUtil.toOracleClassName(tableName);

        sb.append("<update id=\"" + methodName + "\" parameterType=\""
                + parameterType + "\">");
        sb.append(LINE);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("update " + oracleTableName);
        sb.append(LINE);
        sb.append("<set>");
        sb.append(LINE);
        // To do
        for (int i = 0; i < columnCount; i++) {
            String columnName = tableColumns.get(i).getColumnName();
            String column = columnName;
            String property = dealLine(columnName);
            // if (!TableUtil.specialColumn.contains(column) ||
            // column.equals("")
            // || column.equals("UPDATE_BY_ID")
            // || column.equals("UPDATE_BY_NAME")) {
            // sb.append(TAB);
            // sb.append(column + "=#{" + property + "}" + ",");
            // sb.append(LINE);
            // } else if (column.equals("UPDATE_TIME")) {
            // sb.append(TAB);
            // sb.append(column + "=now()" + ",");
            // sb.append(LINE);
            // }
            sb.append(TAB);
            sb.append(column + "=#{" + property + "}" + ",");
            sb.append(LINE);
        }
        sb.deleteCharAt(sb.length() - 3);
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

        String methodName = "delete" + TableUtil.toOracleClassName(tableName)
                + "List";
        String parameterType = TableUtil.OraclePoPath
                + dealLine(tableName).toLowerCase() + "."
                + TableUtil.toOracleClassName(tableName);
        sb.append("<delete id=\"" + methodName + "\" parameterType =\""
                + parameterType + "\">");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("delete from " + oracleTableName);
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

        String methodName = "delete" + TableUtil.toOracleClassName(tableName)
                + "ListByIds";
        sb.append("<delete id=\"" + methodName + "\" parameterType =\""
                + "java.lang.String" + "\">");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("delete from " + oracleTableName);
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

        String methodName = "update" + TableUtil.toOracleClassName(tableName)
                + "List";
        String parameterType = "java.util.List";
        sb.append("<update id=\"" + methodName + "\" parameterType=\""
                + parameterType + "\">");
        sb.append(LINE);

        // sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
        // + "\" index= \"" + "index" + "\" separator=\"" + ";" + "\" >");

        sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
                + "\" index= \"" + "index" + "\" open=\"" + "begin"
                + "\" close=\"" + "end;" + "\" separator=\"" + "" + "\" >");
        sb.append(LINE);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("update " + oracleTableName);
        sb.append(LINE);
        sb.append("set");
        sb.append(LINE);
        // To Do
        for (int i = 0; i < columnCount; i++) {
            String columnName = tableColumns.get(i).getColumnName();
            String column = columnName;
            String property = dealLine(columnName);
            // if (!TableUtil.specialColumn.contains(column) ||
            // column.equals("")
            // || column.equals("UPDATE_BY_ID")
            // || column.equals("UPDATE_BY_NAME")) {
            // sb.append(TAB);
            // sb.append(column + "=#{item." + property + "}" + ",");
            // sb.append(LINE);
            // } else if (column.equals("UPDATE_TIME")) {
            // sb.append(TAB);
            // sb.append(column + "=now()" + ",");
            // sb.append(LINE);
            // }
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
        sb.append(primaryKeyColumn + "=#{item." + primaryKeyProperty + "};");
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

        String methodName = "insert" + TableUtil.toOracleClassName(tableName)
                + "List";
        String parameterType = "java.util.List";
        sb.append("<select id=\"" + methodName + "\" parameterType=\""
                + parameterType + "\">");
        sb.append(LINE);
        sb.append("begin");
        sb.append(LINE);
        // sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
        // + "\" index= \"" + "index" + "\" separator=\"" + "union "
        // + "\" >");
        sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
                + "\" index=\"" + "index" + "\" separator=\"" + ";" + "\" >");

        sb.append(LINE);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("insert into " + oracleTableName);

        sb.append(LINE);
        sb.append("(");
        for (int i = 0; i < columnCount; i++) {
            String columnName = tableColumns.get(i).getColumnName();
            String column = columnName;

            // if (!TableUtil.specialColumn.contains(column) ||
            // column.equals("")
            // || column.equals("CREATE_BY_ID")
            // || column.equals("CREATE_BY_NAME")) {
            // sb.append(TAB);
            // sb.append(column + ",");
            // sb.append(LINE);
            // } else if (column.equals("CREATE_TIME")) {
            // sb.append(TAB);
            // sb.append(column + ",");
            // sb.append(LINE);
            // }
            sb.append(TAB);
            sb.append(column + ",");
            sb.append(LINE);

        }
        TableUtil.trimAnyOne(sb, ",");
        sb.append(")values(");

        // sb.append("<foreach collection=\"" + "list" + "\" item=\"" + "item"
        // + "\" index= \"" + "index" + "\" open=\"" + "(" + "\" close=\""
        // + ")" + "\" separator=\"" + "union all" + "\" >");

        sb.append(LINE);
        // sb.append("select");
        for (int i = 0; i < columnCount; i++) {
            String columnName = tableColumns.get(i).getColumnName();
            String property = dealLine(columnName);
            // String dataType = tableColumns.get(i).getDataType();
            sb.append(TAB);
            sb.append("#{item." + property + "}" + ",");
            sb.append(LINE);
            // if (!TableUtil.specialColumn.contains(property)
            // || property.equals("createById")
            // || property.equals("createByName")) {
            // sb.append(TAB);
            //
            // // ***数据类型为text时resultMap需要转化***
            // if ("TEXT".equals(dataType) || "TINYTEXT".equals(dataType)
            // || "MEDIUMTEXT".equals(dataType)
            // || "LONGTEXT".equals(dataType)) {
            // // sb.append("#{item." + property + ",jdbcType=" + "CLOB"
            // // + "}" + ",");
            // sb.append("#{item." + property + "}" + ",");
            //
            // }
            //
            //
            //
            // sb.append("#{item." + property + "}" + ",");
            // sb.append(LINE);
            //
            // } else if (property.equals("createTime")) {
            // sb.append(TAB);
            // // sb.append("now()" + ",jdbcType=" + dataType + ",");
            // sb.append("sysdate" + ",");
            // sb.append(LINE);
            // }

        }
        TableUtil.trimAnyOne(sb, ",");
        // sb.append("from dual");

        sb.append(")");
        sb.append("</foreach>");
        sb.append(LINE);
        sb.append(";end;");
        sb.append(LINE);
        sb.append("</select>");
        sb.append(LINE);
    }

    // 条件分页查询
    private void genSelectQuery(List<TableColumn> tableColumns,
                                int columnCount, StringBuffer sb, String tableName)
            throws SQLException {
        // parameterType="java.util.Map"

        String methodName = "find" + TableUtil.toOracleClassName(tableName)
                + "Query";
        // String resultMap = dealLine(tableName).toLowerCase();
        String className = TableUtil.toOracleClassName(tableName);
        String resultMap = TableUtil.toParameterName(className);
        sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
                + "\">");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("select * from " + oracleTableName);
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
            //
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

        String methodName = "find" + TableUtil.toOracleClassName(tableName)
                + "QueryPage";
        // String resultMap = dealLine(tableName).toLowerCase();
        String className = TableUtil.toOracleClassName(tableName);
        String resultMap = TableUtil.toParameterName(className);

        sb.append("<select id=\"" + methodName + "\" resultMap=\"" + resultMap
                + "\">");
        sb.append(LINE);
        sb.append(TAB);
        sb.append("select t2.* from");
        sb.append(LINE);
        sb.append(TAB);
        sb.append("(select t1.*,rownum as linenum from");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("(select * from " + oracleTableName);
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
            //
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

        sb
                .append("</where>) t1 where rownum &lt;=(#{startIndex} + #{limit})) t2");
        // sb.append("</where>) t1 where rownum<=(<![CDATA[(#{startIndex}) + (#{limit})]]> )) t2");
        // <if
        // test="startIndex !=null and startIndex!=''and limit !=null and limit!=''">
        // limit #{startIndex},#{limit}
        // </if>
        sb.append(LINE);
        sb.append(TAB);
        sb.append("where");

        sb.append(LINE);
        sb.append(TAB);
        sb.append("t2.linenum &gt; (#{startIndex})");
        sb.append(LINE);
        sb.append(TAB);
        // sb.append(" <if test=\"" + "startIndex"
        // + "!=null and startIndex!='' and " + "limit" + "!=null and "
        // + "limit" + "!=''\"> ");
        // sb.append(LINE);
        // sb.append(TAB);
        // sb.append(TAB);
        // sb.append("limit #{startIndex},#{limit}");
        // sb.append(LINE);
        // sb.append(TAB);
        // sb.append("</if>");
        // sb.append(LINE);
        sb.append("</select>");
        sb.append(LINE);
    }

    // 条件查询总条数
    private void genCountQuery(List<TableColumn> tableColumns, int columnCount,
                               StringBuffer sb, String tableName) throws SQLException {
        // findFnUserById
        // findFnUserList

        // /parameterType="java.util.Map"

        // String resultMap = dealLine(tableName).toLowerCase();

        String methodName = "findCount"
                + TableUtil.toOracleClassName(tableName) + "Query";

        String resultMap = "java.lang.Integer";
        sb.append("<select id=\"" + methodName + "\" resultType=\"" + resultMap
                + "\">");
        sb.append(LINE);
        sb.append(TAB);
        String oracleTableName = genCreateOracleService
                .transferTableName(tableName);
        sb.append("select count(*) from " + oracleTableName);
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
            //
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

    private String dealLine(String tableName) {
        tableName = TableUtil.dealName(tableName);
        return tableName;
    }

}
