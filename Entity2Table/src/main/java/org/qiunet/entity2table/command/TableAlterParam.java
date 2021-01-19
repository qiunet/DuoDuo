package org.qiunet.entity2table.command;

/***
 * 修改表结构使用
 *
 * qiunet
 * 2019-08-22 11:09
 ***/
public class TableAlterParam {
	private String dbSourceName;
	private String tableName;

	private FieldParam field;

	private String dbName;

	private boolean splitTable;

	public TableAlterParam(String dbSourceName, String tableName, FieldParam field, boolean splitTable) {
		this.dbSourceName = dbSourceName;
		this.splitTable = splitTable;
		this.tableName = tableName;
		this.field = field;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public FieldParam getField() {
		return field;
	}

	public void setField(FieldParam field) {
		this.field = field;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public boolean isSplitTable() {
		return splitTable;
	}

	public String getDbSourceName() {
		return dbSourceName;
	}
}
