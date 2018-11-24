package com.jceif.data.dao.mapper;

import java.util.List;
import java.util.Map;

import com.jceif.data.model.Table;
import com.jceif.data.model.TableColumn;


public interface TableMapper {
	 Table getTableDefine(String tableName, String schemaName);
	 List<Table> getTableDefineBySchema(String schemaName);
	 List<TableColumn> getTableColumnInfo(Map map);
}
