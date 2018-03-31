package service.table;


import model.table.Table;
import model.table.TableColumn;


import java.util.List;


public interface TableService {
	 Table getTableDefine(String tableName, String schemaName);
	 List<Table> getTableDefineBySchema(String schemaName);
	 List<TableColumn> getMysqlTableColumnInfo(String tableName, String schemaName);
}
