package impl.table;


import model.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import service.table.GenCreateOracleService;
import service.table.GenDubboXMLService;
import service.table.TableService;
import tool.TableUtil;

import java.util.List;


@Repository("genDubboXMLService")
public class GenDubboXMLImpl implements GenDubboXMLService {

	private static final String LINE = "\r\n";
	private static final String TAB = "	";

	@Autowired
	@Qualifier("tableService")
	private TableService tableService;

	@Autowired
	@Qualifier("genCreateOracleService")
	private GenCreateOracleService genCreateOracleService;


	public String geneCustomerXML(String schemaName, String target)
			throws Exception {
		StringBuffer fileContent = new StringBuffer();
		fileContent.append(xmlHead());

		// <dubbo:application name="callcenter-dubbo-consumer" />
		// <dubbo:registry address="zookeeper://zookeeper.mxd.com:2181" />
		// <dubbo:consumer timeout="1200000" />
		fileContent.append(LINE
				+ "<dubbo:application name=\"callcenter-dubbo-consumer\" />");

		fileContent
				.append(LINE
						+ "<dubbo:registry address=\"zookeeper://zookeeper.mxd.com:2181\" />");

		fileContent.append(LINE + "<dubbo:consumer timeout=\"1200000\" />");

		// <dubbo:reference id="remoteCapitalService"
		// interface="com.minxindai.callcenter.remote.service.capital.RemoteCapitalService"
		// />
		// <dubbo:reference id="remoteCustomerService"
		// interface="com.minxindai.callcenter.remote.service.customer.RemoteCustomerService"/>
		// <dubbo:reference id="remoteCustomerHisService"
		// interface="com.minxindai.callcenter.remote.service.customerhis.RemoteCustomerHisService"/>

		List<Table> tables = tableService.getTableDefineBySchema(schemaName);
		for (Table table : tables) {
			String tableName = table.getTableName();
			fileContent.append(LINE + dubboReference(tableName, target));
		}

		fileContent.append(LINE + "</beans>");
		return fileContent.toString();
	}


	public String geneProducerXML(String schemaName, String target)
			throws Exception {
		StringBuffer fileContent = new StringBuffer();
		fileContent.append(xmlHead());

//		 <dubbo:application name="callcenter-dubbo-provider"/>
//		    <dubbo:registry address="zookeeper://zookeeper.mxd.com:2181"/>
//		    <dubbo:protocol name="dubbo" port="20880"/>
		
		
		fileContent.append(LINE
				+ "<dubbo:application name=\"callcenter-dubbo-provider\" />");

		fileContent
				.append(LINE
						+ "<dubbo:registry address=\"zookeeper://zookeeper.mxd.com:2181\" />");

		fileContent.append(LINE + "<dubbo:consumer timeout=\"1200000\" />");
		
		
		List<Table> tables = tableService.getTableDefineBySchema(schemaName);
		
		for (Table table : tables) {
			String tableName = table.getTableName();
			fileContent.append(LINE + dubboService(tableName, target));
		}
		fileContent.append(LINE + "</beans>");
		return fileContent.toString();
	}

	public String xmlHead() {
		StringBuffer sb = new StringBuffer();

		// <beans xmlns="http://www.springframework.org/schema/beans"
		// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
		// xsi:schemaLocation="http://www.springframework.org/schema/beans
		// http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
		// http://code.alibabatech.com/schema/dubbo
		// http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
		sb.append("<?xml version=\"" + "1.0" + "\" encoding=\"" + "UTF-8"
				+ "\"?>");
		sb.append(LINE + "<beans xmlns=\""
				+ "http://www.springframework.org/schema/beans" + "\"");
		sb.append(LINE + "xmlns:xsi=\""
				+ "http://www.w3.org/2001/XMLSchema-instance" + "\"");
		sb.append(LINE + "xmlns:dubbo=\""
				+ "http://code.alibabatech.com/schema/dubbo" + "\"");
		sb
				.append(LINE
						+ "xsi:schemaLocation=\""
						+ "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd");
		sb
				.append(LINE
						+ "http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd");
		sb.append(" \"> ");

		return sb.toString();
	}

	public String dubboReference(String tableName, String target) {
		StringBuffer sb = new StringBuffer();
		String className = "";
		String servicePath = "";
		if (target.equalsIgnoreCase(TableUtil.databaseTypeMysql)) {
			className = TableUtil.toClassName(tableName);
			servicePath = TableUtil.MysqlServicePath;
		} else if (target.equalsIgnoreCase(TableUtil.databaseTypeOracle)) {
			className = TableUtil.toOracleClassName(tableName);
			servicePath = TableUtil.OracleServicePath;
		}
		String paramName = TableUtil.toParameterName(className);

		// <dubbo:reference id="remoteCustomerHisService"
		// interface="com.minxindai.callcenter.remote.service.customerhis.RemoteCustomerHisService"/>

		sb.append(LINE + "<dubbo:reference id=\"" + paramName + "Service\"");
		sb.append(" interface=\"" + servicePath +paramName.toLowerCase()+"."+ className + "Service\"/>");

		return sb.toString();
	}

	public String dubboService(String tableName, String target) {
		StringBuffer sb = new StringBuffer();
		String className = "";
		String servicePath = "";
		if (target.equalsIgnoreCase(TableUtil.databaseTypeMysql)) {
			className = TableUtil.toClassName(tableName);
			servicePath = TableUtil.MysqlServicePath;
		} else if (target.equalsIgnoreCase(TableUtil.databaseTypeOracle)) {
			className = TableUtil.toOracleClassName(tableName);
			servicePath = TableUtil.OracleServicePath;
		}
		String paramName = TableUtil.toParameterName(className);
		// <bean id="remoteCustomerHisService"
		// class="com.minxindai.callcenter.remote.service.customerhis.impl.RemoteCustomerHisServiceImpl"/>
		// <dubbo:service ref="remoteCustomerHisService"
		// interface="com.minxindai.callcenter.remote.service.customerhis.RemoteCustomerHisService"/>
		sb.append(LINE + "<bean id=\"" + paramName + "Service\"");

		sb.append(" class=\"" + servicePath + paramName.toLowerCase()
				+ ".impl." + className + "ServiceImpl\"/>");
		sb.append(LINE+"<dubbo:service ref=\""+paramName+"Service\"");
		sb.append(" interface=\""+servicePath+paramName.toLowerCase()+"."+className+"Service\"/>");

		return sb.toString();
	}
}
