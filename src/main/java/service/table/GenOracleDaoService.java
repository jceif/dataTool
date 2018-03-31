package service.table;

import org.springframework.stereotype.Service;

public interface GenOracleDaoService {
	   String geneMapper(String tableName, String schemaName) throws Exception;
}
