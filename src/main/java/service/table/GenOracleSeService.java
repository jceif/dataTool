package service.table;

import org.springframework.stereotype.Service;


public interface GenOracleSeService {
	   String geneService(String tableName, String schemaName) throws Exception;

	   String geneServiceImpl(String tableName, String schemaName) throws Exception;


}
