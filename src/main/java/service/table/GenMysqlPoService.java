package service.table;

import org.springframework.stereotype.Service;


public interface GenMysqlPoService {
	   String geneJavaBean(String tableName, String schemaName) throws Exception;
}
