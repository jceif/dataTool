package service.table;

import org.springframework.stereotype.Service;


public interface GenMysqlDaoService {
	   String geneMapper(String tableName, String schemaName) throws Exception;
}
