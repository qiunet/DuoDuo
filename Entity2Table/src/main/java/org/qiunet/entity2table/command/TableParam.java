package org.qiunet.entity2table.command;

import java.util.List;

/***
 * 修改表结构使用
 * qiunet
 * 2019-08-22 11:09
 ***/
public class TableParam {

	private String tableName;

	private final List<FieldParam> fields;

	private String dbName;

	private final boolean splitTable;

	private final String dbSource;

	public TableParam(String tableName, List<FieldParam> fields, boolean splitTable, String dbSource) {
		this.splitTable = splitTable;
		this.dbSource = dbSource;
		this.tableName = tableName;
		this.fields = fields;
	}

	public String [] getFieldNames(){
		return fields.stream().map(FieldParam::getFieldName).toArray(String[]::new);
	}

	public String getDbSource() {
		return dbSource;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<FieldParam> getFields() {
		return fields;
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
