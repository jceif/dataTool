package model.table;

import java.io.Serializable;

import org.springframework.stereotype.Repository;


public class TableColumn implements  Serializable{
	// 列名
	private String columnName;

	// 数据类型
	private String dataType;
	
	// 字符长度
	private long characterMaximumLength;

	// 字节长度
	private long characterOctetLength;

	// 整数长度
	private int numericPrecision;

	// 小数长度
	private int numericScale;
	
	// 注释
	private String columnComment;

	// 列key
	private String columnKey;
	
	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public long getCharacterMaximumLength() {
		return characterMaximumLength;
	}

	public void setCharacterMaximumLength(long characterMaximumLength) {
		this.characterMaximumLength = characterMaximumLength;
	}

	public long getCharacterOctetLength() {
		return characterOctetLength;
	}

	public void setCharacterOctetLength(long characterOctetLength) {
		this.characterOctetLength = characterOctetLength;
	}

	public int getNumericPrecision() {
		return numericPrecision;
	}

	public void setNumericPrecision(int numericPrecision) {
		this.numericPrecision = numericPrecision;
	}

	public int getNumericScale() {
		return numericScale;
	}

	public void setNumericScale(int numericScale) {
		this.numericScale = numericScale;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

}
