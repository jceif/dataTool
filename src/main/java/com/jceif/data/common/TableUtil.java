package com.jceif.data.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TableUtil {


	public static final String MysqlPoPath = "com.violin.com.jceif.data.model.";
	public static final String MysqlMapperPath = "com.violin.com.jceif.data.dao.mapper.";
	public static final String MysqlServicePath = "com.violin.com.jceif.data.service.";
	public static final String MysqlServiceImplPath = "com.violin.serviceImpl.";

	public static final String OraclePoPath = "com.violin.dc.com.jceif.data.model.";
	public static final String OracleMapperPath = "com.violin.dc.oracle.mapper.";
	public static final String OracleServicePath = "com.violin.dc.com.jceif.data.service.oracle.";
	public static final String OracleServiceImplPath = "com.violin.dc.com.jceif.data.service.oracle.com.jceif.data.impl.";

	public static final String databaseTypeOracle = "oracle";
	public static final String databaseTypeMysql = "mysql";

	public static final String pageUtilPath = "com.violin.dc.pager.PageUtil;";

	public static final String controllerPath = "com.violin.com.jceif.data.controller.";

	public static final String handlerPath = "com.violin.dc.handler.HandlerClob";

	public static Set<String> specialColumn = new HashSet<String>();

	public static Map<String, String> typeMap = new HashMap<String, String>();

	static {
		typeMap.put("varchar", "String");
		typeMap.put("integer", "Integer");
		typeMap.put("float", "float");
		typeMap.put("timestamp", "Date");
		typeMap.put("char", "String");
		typeMap.put("datetime", "Date");
		typeMap.put("int", "Integer");
		typeMap.put("decimal", "Double");
		typeMap.put("VARCHAR", "String");
		typeMap.put("INTEGER", "Integer");
		typeMap.put("FLOAT", "float");
		typeMap.put("TIMESTAMP", "Date");
		typeMap.put("CHAR", "String");
		typeMap.put("DATETIME", "Date");
		typeMap.put("INT", "Integer");
		typeMap.put("DECIMAL", "Double");
	}
	public static Map<String, String> importMap = new HashMap<String, String>();
	static {
		importMap.put("Date", "import java.util.Date;\n");
		importMap.put("Timestamp", "import java.sql.Timestamp;\n");
		importMap.put("date", "import java.util.Date;\n");
		importMap.put("timestamp", "import java.sql.Timestamp;\n");
	}

	public static String dealName(String name) {
		boolean f = true;
		name = name.toLowerCase();
		while (f) {
			if (name.contains("_")) {
				int b = name.indexOf("_");
				String temp = name.substring(b, b + 2);
				String temp2 = name.substring(b + 1, b + 2);
				name = name.replace(temp, temp2.toUpperCase());
			} else {
				f = false;
			}
		}
		return name;
	}

	/**
	 * 正常的类名
	 * 
	 * @param Table_Name
	 *            表名 ：Class_Name
	 * @return 类名：ClassName
	 */
	public static String toClassName(String Table_Name) {

		String tableName = Table_Name.toLowerCase();
		String[] names = tableName.split("_");
		StringBuffer sb = new StringBuffer();

		for (String s : names) {
			String fs = s.substring(0, 1).toUpperCase();
			String es = s.substring(1, s.length());
			sb.append(fs).append(es);
		}
		tableName = sb.toString();
		return tableName;
	}

	/**
	 * oracle的类名
	 * 
	 * @param Table_Name
	 *            表名 ：Class_Name
	 * @return 类名：OracleClassName
	 */

	public static String toOracleClassName(String Table_Name) {

		String tableName = dealName("Oracle_" + Table_Name);
		String[] names = tableName.split("_");
		StringBuffer sb = new StringBuffer();

		for (String s : names) {
			String fs = s.substring(0, 1).toUpperCase();
			String es = s.substring(1, s.length());
			sb.append(fs).append(es);
		}
		tableName = sb.toString();
		return tableName;
	}

	/**
	 * 首字母小写的驼峰命名
	 * 
	 * @param className
	 *            类名 ：ClassName
	 * @return 首字母小写：className
	 */
	public static String toParameterName(String className) {
		return className.substring(0, 1).toLowerCase()
				+ className.substring(1, className.length());
	}

	/**
	 * 文件生成（包括多级文件夹）
	 */

	public static void buildFile(String importPath, String fileName,
			String fileContent) {
		System.out.println("importPath=" + importPath);
		String filePath = getPath(importPath);
		System.out.println("filePath=" + filePath + "fileName=" + fileName);
		try {
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(filePath, fileName);
	
			OutputStreamWriter oWriter = new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8");

			System.out.println("文件真实路径=" + file.getAbsolutePath());

			PrintWriter pw = new PrintWriter(oWriter);

			String fi = new String(fileContent.getBytes("UTF-8"), "UTF-8");
			pw.println(fi);
			pw.close();
		} catch (Exception e) {
			System.out.println("err" + e.getMessage());
		}
	}

	/**
	 * 转换
	 * 
	 * @param com
	 *            .violin.dc.com.jceif.data.model.xxx.
	 * @return com\\myviolin\\dc\\com.jceif.data.model\\xxx\\
	 */

	public static String getPath(String importPath) {
		String re = importPath.replace(".", File.separator);
		return re;
	}

	/**
	 * 获得java类型
	 * 
	 * @param columnName
	 *            列名
	 * @param dataType
	 *            类型
	 * @return
	 */
	public static String getJavaType(String columnName, String dataType,
			String databaseType, String tableName) {
		String strRtn = "";
		// 整型
		if ("TINYINT".equals(dataType) || "SMALLINT".equals(dataType)
				|| "MEDIUMINT".equals(dataType) || "INT".equals(dataType)
				|| "tinyint".equals(dataType) || "smallint".equals(dataType)
				|| "mediumint".equals(dataType) || "int".equals(dataType)) {
			strRtn = "int";
		} else if ("BIGINT".equals(dataType) || "bigint".equals(dataType)) {
			strRtn = "long";
		} else if ("DECIMAL".equals(dataType) || "DOUBLE".equals(dataType)
				|| "decimal".equals(dataType) || "double".equals(dataType)) {
			strRtn = "Double";

		} else if ("FLOAT".equals(dataType) || "float".equals(dataType)) {
			strRtn = "float";

		} else if ("DATE".equals(dataType) || "DATETIME".equals(dataType)
				|| "TIME".equals(dataType) || "YEAR".equals(dataType)
				|| "date".equals(dataType) || "datetime".equals(dataType)
				|| "time".equals(dataType) || "year".equals(dataType)) {
			strRtn = "Date";
		} else if ("TIMESTAMP".equalsIgnoreCase(dataType)) {
			strRtn = "Timestamp";

		} else if ("CHAR".equals(dataType) || "char".equals(dataType)) {
			strRtn = "String";
		} else if ("VARCHAR".equals(dataType) || "varchar".equals(dataType)) {
			strRtn = "String";
		} else if ("TEXT".equals(dataType) || "TINYTEXT".equals(dataType)
				|| "MEDIUMTEXT".equals(dataType) || "LONGTEXT".equals(dataType)
				|| "text".equals(dataType) || "tinytext".equals(dataType)
				|| "mediumtext".equals(dataType) || "longtext".equals(dataType)) {
			strRtn = "String";
		} else if ("TINYBLOB".equals(dataType) || "BLOB".equals(dataType)
				|| "MEDIUMBLOB".equals(dataType) || "LONGBLOB".equals(dataType)
				|| "tinyblob".equals(dataType) || "blob".equals(dataType)
				|| "mediumblob".equals(dataType) || "longblob".equals(dataType)) {
			// blob 类型咱不处理
		} else if ("BOOL".equals(dataType) || "BOOLEAN".equals(dataType)
				|| "bool".equals(dataType) || "boolean".equals(dataType)) {
			// bool 类型暂不处理
		} else if ("ENUM".equals(dataType) || "SET".equals(dataType)
				|| "enum".equals(dataType) || "set".equals(dataType)) {
			// 枚举集合 类型暂不处理
		}
		if (databaseType.equalsIgnoreCase(TableUtil.databaseTypeOracle)) {
			String strCompare = columnName.toUpperCase();
			if (isDateColumn(tableName, columnName, dataType)) {
				strRtn = "Date";
			}
		}

		// if (strCompare.indexOf("_TIME") > -1
		// || strCompare.indexOf("_time") > -1) {
		// // 这类修改过 最终版本是Date
		// // strRtn = "Date";
		//
		// System.out.println("columnName=" + columnName + "dataType="
		// + dataType);
		// if ("DATE".equals(dataType) || "date".equals(dataType)
		// || "DATETIME".equals(dataType) || "datetime".equals(dataType)) {
		// strRtn = "Date";
		// } else {
		//
		// if ("oracle".equals(databaseType)) {
		// strRtn = "Date";
		// } else if ("mysql".equals(databaseType)) {
		// strRtn = "String";
		// }
		//
		// }
		// }

		return strRtn;
	}

	/**
	 * 判断是否需要在controller中对日期进行转换
	 * 
	 * @param mySqlJavaType
	 *            mysql 的po中 某个属性的java类型
	 * @param oracleJavaType
	 *            oracle 的po中 某个属性的java类型
	 * @return
	 */

	public static boolean beanJavaTypeConvert(String mySqlJavaType,
			String oracleJavaType) {
		boolean b = false;
		if ("String".equals(mySqlJavaType) && "Date".equals(oracleJavaType)) {
			b = true;
		}
		return b;
	}

	/**
	 * 判断列是否需要转换成日期型
	 * 
	 * @param columnName
	 * @return
	 */
	public static boolean isDateColumn(String tableName, String columnName,
			String dataType) {
		boolean bolDateCol = false;
		String strCompare = columnName.toUpperCase();
		if (strCompare.indexOf("TIME") > -1) {
			bolDateCol = true;
		}

		if ("INCOME".equalsIgnoreCase(tableName)
				&& "DEADLINE".equalsIgnoreCase(columnName)) {
			bolDateCol = true;
		}

		if (!("CHAR".equalsIgnoreCase(dataType)
				|| "VARCHAR".equalsIgnoreCase(dataType) || (dataType
				.toUpperCase().indexOf("TEXT")) > -1)) {
			bolDateCol = false;
		}

		return bolDateCol;
	}

	/**
	 * 去掉任何的最后出现的某字符
	 * 
	 * @param sb
	 *            需要处理的stringbuff
	 * @param string
	 *            目标字符 比如 ， .
	 */
	public static void trimAnyOne(StringBuffer sb, String string) {
		int i = sb.lastIndexOf(string);
		sb.delete(i, i + 1);
	}

	/**
	 * 是否要以增量的形式进行插入
	 * 
	 * @key 表名
	 * @value 字段名
	 * 
	 *        根据某个字段 从数据库中查寻出近两天 插入的
	 * 
	 *        在生成controller时 如果该表以增量的形式插入，则不执行删除全部
	 */
	public static HashMap<String, String> incrementMap = new HashMap<String, String>();
	static {

		incrementMap.put("access_log2015_01", "CREATE_TIME");
		incrementMap.put("access_log2014_12", "CREATE_TIME");
		incrementMap.put("access_log2014_11", "CREATE_TIME");
		incrementMap.put("access_log2014_10", "CREATE_TIME");
		incrementMap.put("access_log2014_09", "CREATE_TIME");

		incrementMap.put("account_20150301", "CREATE_TIME");
		incrementMap.put("account_20150101", "CREATE_TIME");
		incrementMap.put("account_20150201", "CREATE_TIME");
		incrementMap.put("account_20150101", "CREATE_TIME");
		incrementMap.put("account_20141201", "CREATE_TIME");
		incrementMap.put("account_20141101", "CREATE_TIME");
		incrementMap.put("account_20141001", "CREATE_TIME");
		incrementMap.put("account_20140901", "CREATE_TIME");
		incrementMap.put("account_log", "CREATE_TIME");
	}

}
