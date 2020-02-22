package org.qiunet.entity2table.command;

/***
 * 修改表结构使用
 *
 * qiunet
 * 2019-08-22 11:09
 ***/
public class TableParam {

	private String tableName;

	private FieldParam field;

	private String dbName;

	private boolean splitTable;

	private boolean defaultDb;

	public TableParam(String tableName, FieldParam field, boolean splitTable, boolean defaultDb) {
		this.splitTable = splitTable;
		this.defaultDb = defaultDb;
		this.tableName = tableName;
		this.field = field;
	}

	public boolean isDefaultDb() {
		return defaultDb;
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
}
