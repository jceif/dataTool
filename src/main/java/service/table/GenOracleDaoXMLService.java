package service.table;

import org.springframework.stereotype.Service;


public interface GenOracleDaoXMLService {
	   String geneMybatisXML(String tableName, String schemaName) throws Exception;
}
