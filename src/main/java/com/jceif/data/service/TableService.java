package com.jceif.data.service;


import com.jceif.data.model.Table;
import com.jceif.data.model.TableColumn;


import java.util.List;


public interface TableService {
	 Table getTableDefine(String tableName, String schemaName);
	 List<Table> getTableDefineBySchema(String schemaName);
	 List<TableColumn> getMysqlTableColumnInfo(String tableName, String schemaName);
}
