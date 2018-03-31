package model.table;


import java.io.Serializable;

import org.springframework.stereotype.Repository;


public class Table implements  Serializable{
	// 表名（英文）
	private String tableName;
	// 注释
	private String tableComment;


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}


}
