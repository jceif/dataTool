package dao.table;

import java.util.List;
import java.util.Map;

import model.table.Table;
import model.table.TableColumn;


public interface TableMapper {
	 Table getTableDefine(String tableName, String schemaName);
	 List<Table> getTableDefineBySchema(String schemaName);
	 List<TableColumn> getTableColumnInfo(Map map);
}
