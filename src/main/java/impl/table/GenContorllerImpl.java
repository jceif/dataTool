package impl.table;


import model.table.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import service.table.GenContorllerService;
import service.table.GenCreateOracleService;
import service.table.TableService;
import tool.TableUtil;

import java.util.List;

@Repository("genContorllerService")
public class GenContorllerImpl implements GenContorllerService {

	private static final String LINE = "\r\n";
	private static final String TAB = "	";

	@Autowired
	@Qualifier("tableService")
	private TableService tableService;

	@Autowired
	@Qualifier("genCreateOracleService")
	private GenCreateOracleService genCreateOracleService;


	public String geneContorller(String tableName, String schemaName)
			throws Exception {
		String className = TableUtil.toClassName(tableName);
		StringBuffer fileContent = new StringBuffer();

		fileContent.append("package " + TableUtil.controllerPath
				+ TableUtil.toClassName(tableName).toLowerCase() + " ;");

		fileContent.append(getImport(tableName));
		fileContent.append(LINE);
		fileContent.append(LINE);
		fileContent.append(TAB + "@Controller");
		fileContent.append(LINE);
		fileContent
				.append(TAB + "@RequestMapping(\"" + "/" + className + "\")");
		fileContent.append(LINE);
		fileContent.append(TAB + "public class " + className + "Controller {");
		fileContent.append(LINE);
		fileContent.append(TAB + TAB + "@Autowired");
		fileContent.append(LINE);
		fileContent.append(TAB + TAB + TableUtil.toClassName(tableName)
				+ "Service "
				+ TableUtil.toParameterName(TableUtil.toClassName(tableName))
				+ "Service;");
		fileContent.append(LINE);
		fileContent.append(TAB + TAB + "@Autowired");
		fileContent.append(LINE);
		fileContent.append(TAB
				+ TAB
				+ TableUtil.toOracleClassName(tableName)
				+ "Service "
				+ TableUtil.toParameterName(TableUtil
						.toOracleClassName(tableName)) + "Service;");
		fileContent.append(LINE);

		fileContent.append(LINE + "@Autowired");
		fileContent.append(LINE
				+ "OracleImDataRecordService oracleImDataRecordService;");

		fileContent.append(getDataShift(tableName));
		fileContent.append(LINE);

		List<TableColumn> mySqlTableColumnList = tableService.getMysqlTableColumnInfo(tableName, schemaName);
		List<TableColumn> oracleTableColumnList = genCreateOracleService.getOracleTableColumnInfo(tableName, schemaName);

		fileContent.append(getCopyBean(tableName, mySqlTableColumnList,
				oracleTableColumnList));

		fileContent.append(LINE);
		fileContent.append("}");
		return fileContent.toString();
	}

	private String getImport(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE
				+ "import com.minxindai.dc.controller.oracle.OneController;");

		sb.append(LINE + "import com.minxindai.common.util.GUIDUtil;");
		sb
				.append(LINE
						+ "import com.minxindai.dc.model.imdatarecord.OracleImDataRecord;");

		sb.append(LINE + "import java.text.ParseException;");
		sb
				.append(LINE
						+ "import com.minxindai.dc.service.oracle.imdatarecord.OracleImDataRecordService;");

		sb.append(LINE + "import java.text.SimpleDateFormat;");

		sb.append(LINE + "import java.util.ArrayList;");

		sb.append(LINE + "import java.util.List;");

		sb.append(LINE + "import java.util.HashMap;");

		sb.append(LINE + "import java.util.Map;");

		sb.append(LINE + "import java.util.Date;");
		sb.append(LINE);
		sb.append("import " + TableUtil.MysqlServicePath
				+ TableUtil.toClassName(tableName).toLowerCase() + "."
				+ TableUtil.toClassName(tableName) + "Service;");
		sb.append(LINE);
		sb.append("import " + TableUtil.OracleServicePath
				+ TableUtil.toClassName(tableName).toLowerCase() + "."
				+ TableUtil.toOracleClassName(tableName) + "Service;");
		sb.append(LINE);
		sb.append("import " + TableUtil.MysqlPoPath
				+ TableUtil.toClassName(tableName).toLowerCase() + "."
				+ TableUtil.toClassName(tableName) + ";");
		sb.append(LINE);
		sb.append("import " + TableUtil.MysqlPoPath
				+ TableUtil.toClassName(tableName).toLowerCase() + "."
				+ TableUtil.toOracleClassName(tableName) + ";");

		sb.append(LINE);
		sb.append("import " + TableUtil.pageUtilPath);

		sb.append(LINE);
		sb
				.append("import org.springframework.beans.factory.annotation.Autowired;");

		sb.append(LINE);
		sb.append("import org.springframework.stereotype.Controller;");

		sb.append(LINE);
		sb
				.append("import org.springframework.web.bind.annotation.RequestMapping;");

		sb.append(LINE);
		sb
				.append("import org.springframework.web.bind.annotation.ResponseBody;");

		sb.append(LINE);

		return sb.toString();
	}

	private String getDataShift(String tableName) {
		String className = TableUtil.toClassName(tableName);
		String oracleClassName = TableUtil.toOracleClassName(tableName);
		String parameterName = TableUtil.toParameterName(className);
		String oracleParameterName = TableUtil.toParameterName(oracleClassName);
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		sb.append("@RequestMapping(\"" + "/dataShift" + "\")");
		sb.append(LINE);
		sb.append("@ResponseBody");
		sb.append(LINE);
		sb.append("public String dataShift() {");
		sb.append(LINE);
		sb.append(LINE + "int pageSize = OneController.pageSize;");
		// //初始时间
		sb.append(LINE);
		sb.append("Date startDate = new Date();");

		// //
		if (TableUtil.incrementMap.get(tableName) != null) {

			sb.append(LINE
					+ "int count = "
					+ TableUtil.toParameterName(TableUtil
							.toClassName(tableName)) + "Service.findCount"
					+ TableUtil.toClassName(tableName)
					+ "IncrementQuery(new HashMap());");
		} else {
			sb.append(LINE
					+ "int count = "
					+ TableUtil.toParameterName(TableUtil
							.toClassName(tableName)) + "Service.findCount"
					+ TableUtil.toClassName(tableName)
					+ "Query(new HashMap());");
		}

		// //

		sb
				.append(LINE
						+ "int pageCount=count%pageSize==0?count/pageSize:count/pageSize+1;");

		if (TableUtil.incrementMap.get(tableName) == null) {
			sb.append(LINE + TAB + "int de =" + oracleParameterName
					+ "Service.delete" + oracleClassName + "All();");
			sb.append(LINE + TAB + "System.out.print(\"+" + tableName
					+ "+ 删除\" + de + \"行\");");
		}

		sb.append(LINE + "for (int i = 1; i <= pageCount; i++) {");
		sb.append(LINE);
		sb.append(TAB + "System.out.print(\"" + tableName + " 执行第" + "\" + "
				+ "i" + " + \"" + "页 ,共  " + "\" + " + "pageCount +" + "\""
				+ "页" + "\");");
		sb.append(LINE);
		// sb.append(TAB + "PageUtil<" + className + "> " + parameterName
		// + "PageUtil = " + parameterName + "Service.find" + className
		// + "QueryPage(new HashMap(), String.valueOf(i), \"" + "500"
		// + "\");");
		if (TableUtil.incrementMap.get(tableName) != null) {
			sb
					.append(TAB
							+ "PageUtil<"
							+ className
							+ "> "
							+ parameterName
							+ "PageUtil = "
							+ parameterName
							+ "Service.find"
							+ className
							+ "IncrementQueryPage(new HashMap(), String.valueOf(i), String.valueOf(pageSize));");
		} else {
			sb
					.append(TAB
							+ "PageUtil<"
							+ className
							+ "> "
							+ parameterName
							+ "PageUtil = "
							+ parameterName
							+ "Service.find"
							+ className
							+ "QueryPage(new HashMap(), String.valueOf(i), String.valueOf(pageSize));");
		}

		sb.append(LINE);
		sb.append(TAB + "List<" + className + "> " + parameterName + "List = "
				+ parameterName + "PageUtil.getRecords();");
		sb.append(LINE);

		sb.append(TAB + "List<" + oracleClassName + "> " + oracleParameterName
				+ "List = new ArrayList<" + oracleClassName + ">();");
		sb.append(LINE);
		// 循环开始
		sb.append(TAB + "for (" + className + " " + parameterName + " : "
				+ parameterName + "List) {");
		sb.append(LINE);
		sb.append(TAB + TAB + oracleClassName + " " + oracleParameterName
				+ " = new " + oracleClassName + "();");
		sb.append(LINE);
		sb.append(TAB + TAB + "try {");
		sb.append(LINE);
		sb.append(TAB + TAB + "copyBean(" + parameterName + ", "
				+ oracleParameterName + ");");

		sb.append(LINE);
		sb.append(TAB + TAB + oracleParameterName + "List.add("
				+ oracleParameterName + ");");

		sb.append(LINE);
		sb.append(TAB + TAB + "} catch (Exception e) {");
		sb.append(LINE);

		sb.append(TAB + TAB + "System.out.println(\"" + tableName
				+ " \"+i+\"页  转换异常\");");
		sb.append(LINE);
		sb.append(TAB + TAB + "throw new RuntimeException(e);");

		sb.append(LINE);
		sb.append(TAB + "}");

		// 循环结束
		sb.append(LINE + TAB + "}");

		sb.append(LINE + TAB + TAB + "if (" + oracleParameterName
				+ "List.size() > 0) {");

		if (TableUtil.incrementMap.get(tableName) != null) {

			sb.append(LINE + TAB + TAB + TAB + "int deleteRe = "
					+ oracleParameterName + "Service.delete" + oracleClassName
					+ "List(" + oracleParameterName + "List);");
			sb.append(LINE + TAB + TAB + TAB + "System.out.println(\""
					+ tableName + " \"+i+\"页 删除\" + deleteRe + \"行\");");
		}

		// 没有返回值版本
		sb.append(LINE + TAB + TAB + TAB + oracleParameterName
				+ "Service.insert" + oracleClassName + "List("
				+ oracleParameterName + "List);");

		sb.append(LINE + TAB + TAB + TAB + "System.out.println(\"" + tableName
				+ " \"+i+\"页 执行插入 \");");

		sb.append(LINE + TAB + "}");

		// 方法结尾
		sb.append(LINE + TAB + "}");
		// 处理打印时间
		// 结束时间

		sb.append(LINE + "Date finishDate = new Date();");
		sb.append(LINE);

		sb
				.append(LINE
						+ "OracleImDataRecord oracleImDataRecord = new OracleImDataRecord();");
		sb
				.append(LINE
						+ "oracleImDataRecord.setImDataRecordId(GUIDUtil.getGUIDString());");
		sb.append(LINE + "oracleImDataRecord.setTableName(\"" + tableName
				+ "\");");
		sb.append(LINE + "oracleImDataRecord.setStartDate(startDate);");
		sb.append(LINE + "oracleImDataRecord.setFinishDate(finishDate);");
		
		
		//sb.append(LINE + "oracleImDataRecord.setImportNum(count);");
		sb.append(LINE + "oracleImDataRecord.setImportNum(Double.valueOf(count));");
		
		sb
				.append(LINE
						+ "oracleImDataRecordService.insertOracleImDataRecord(oracleImDataRecord);");

		sb.append(LINE + "return \"操作 \" + count + \" rows\";");

		sb.append(LINE);
		sb.append(TAB + "}");
		return sb.toString();

	}

	private String getCopyBean(String tableName,
			List<TableColumn> mySqlTableColumnList,
			List<TableColumn> oracleTableColumnList) {

		String mySqlClassName = TableUtil.toClassName(tableName);
		String mySqlParameterName = TableUtil.toParameterName(mySqlClassName);
		String oracleClassName = TableUtil.toOracleClassName(tableName);
		String oracleParameterName = TableUtil.toParameterName(oracleClassName);
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		sb.append(TAB + "public static void copyBean(" + mySqlClassName + " "
				+ mySqlParameterName + "," + oracleClassName + " "
				+ oracleParameterName + ")throws ParseException {");

		sb.append(LINE);
		sb
				.append("SimpleDateFormat simpleDateFormat =  new SimpleDateFormat(\""
						+ "yyyy-MM-dd HH:mm:sss" + "\");");

		for (int i = 0; i < mySqlTableColumnList.size(); i++) {
			TableColumn mySqlTableColumn = mySqlTableColumnList.get(i);
			String mySqlDataType = mySqlTableColumn.getDataType();
			String mySqlColumnName = mySqlTableColumn.getColumnName();
			String mySqlMethodProperty = TableUtil.toClassName(mySqlColumnName);
			TableColumn oracleTableColumn = oracleTableColumnList.get(i);
			String oracleDataType = oracleTableColumn.getDataType();
			String oracleColumnName = oracleTableColumn.getColumnName();
			String oracleMethodProperty = TableUtil
					.toClassName(oracleColumnName);
			sb.append(LINE);
			String mySqlJavaType = TableUtil.getJavaType(mySqlColumnName,
					mySqlDataType, TableUtil.databaseTypeMysql, tableName);
			String oracleJavaType = TableUtil.getJavaType(oracleColumnName,
					oracleDataType, TableUtil.databaseTypeOracle, tableName);
			boolean b = TableUtil.beanJavaTypeConvert(mySqlJavaType,
					oracleJavaType);
			if (!b) {

				if ("date".equalsIgnoreCase(mySqlJavaType)
						&& "date".equalsIgnoreCase(oracleJavaType)) {
					sb.append(TAB + TAB + "if(" + mySqlParameterName + ".get"
							+ mySqlMethodProperty + "()!=null){");

					sb.append(LINE);
					sb.append(TAB + TAB + TAB + "try{");

					sb
							.append(TAB
									+ TAB
									+ TAB
									+ oracleParameterName
									+ ".set"
									+ oracleMethodProperty
									+ "(simpleDateFormat.parse(simpleDateFormat.format("
									+ mySqlParameterName + ".get"
									+ mySqlMethodProperty + "())));");

					sb.append(LINE);

					sb.append(TAB + TAB + TAB + "} catch (ParseException e) {");
					sb.append(LINE);
					sb.append(TAB + TAB + TAB + "System.out.println(\""
							+ mySqlMethodProperty + "\");");
					sb.append(LINE);
					sb.append(TAB + TAB + TAB + "}");

					sb.append(LINE);
					sb.append(TAB + TAB + "}");

				} else {
					sb.append(LINE);
					sb.append(TAB + TAB + oracleParameterName + ".set"
							+ oracleMethodProperty + "(" + mySqlParameterName
							+ ".get" + mySqlMethodProperty + "());");
				}

			} else {
				sb.append(TAB + "if(" + mySqlParameterName + ".get"
						+ mySqlMethodProperty + "()!=null&&!\"\".equals("
						+ mySqlParameterName + ".get" + mySqlMethodProperty
						+ "())){");
				sb.append(LINE);

				sb.append(TAB + "String str" + mySqlMethodProperty + " = "
						+ mySqlParameterName + ".get" + mySqlMethodProperty
						+ "().replace(\"/\", \"-\");");

				sb.append(LINE + TAB + "if (str" + mySqlMethodProperty
						+ ".contains(\"-\")) {");

				sb.append(LINE + TAB + TAB + oracleParameterName + ".set"
						+ oracleMethodProperty + "(simpleDateFormat.parse(str"
						+ mySqlMethodProperty + "));");

				sb.append(LINE + TAB + "} else {");

				sb.append(LINE + TAB + TAB + oracleParameterName + ".set"
						+ oracleMethodProperty + "(new Date(Long.valueOf(str"
						+ mySqlMethodProperty + ")));");
				sb.append(LINE + TAB + "}");

				sb.append(LINE + TAB + "}");
			}

		}
		sb.append(LINE);
		sb.append(TAB + "}");
		return sb.toString();
	}
}
