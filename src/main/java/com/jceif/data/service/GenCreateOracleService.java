package com.jceif.data.service;


import com.jceif.data.model.TableColumn;

import java.util.List;

public interface GenCreateOracleService {
	 List<TableColumn> getOracleTableColumnInfo(String tableName, String schemaName);
	// 生成创建表脚本(把long型的日期转换成date)
	 String generateCreateTableSpecial(String tableName, String schemaName);
	
	// 生成创建表脚本(直接转换，不把long型的日期转换成date)
	 String generateCreateTableNospecial(String tableName, String schemaName);
	
	// 生成表注释脚本
	 String generateComments(String tableName, String schemaName);
	// 生成插入语句
     String generateInsert(String tableName, String schemaName);
	/**
	 * 有些关键字不能作为表名,所以转换一下
	 * @param tableName
	 * @return
	 */
	 String transferTableName(String tableName);
}
