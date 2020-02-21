package org.qiunet.entity2table.command;

import java.util.List;
import java.util.stream.Collectors;

/***
 * 创建表结构使用
 *
 * qiunet
 * 2019-08-22 11:09
 ***/
public class TableCreateParam {

	private String tableName;

	private String comment;
	/***
	 * 只有第一次建表判断,是否需要分表
	 */
	private boolean splitTable;

	/**
	 * 只有第一次建表判断,
	 * 是否是默认db的表.
	 * 是的话, 就不会按照分库的方式搞.
	 * 否则 会判断是否有分库, 没有才会查找默认db
	 */
	private boolean defaultDb;

	private List<FieldParam> fields;

	private String dbName;

	public TableCreateParam(String tableName, String comment, List<FieldParam> fields, boolean splitTable, boolean defaultDb) {
		this.splitTable = splitTable;
		this.defaultDb = defaultDb;
		this.tableName = tableName;
		this.comment = comment;
		this.fields = fields;
	}

	public boolean isDefaultDb() {
		return defaultDb;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<FieldParam> getFields() {
		return fields;
	}

	public void setFields(List<FieldParam> fields) {
		this.fields = fields;
	}

	public boolean isSplitTable() {
		return splitTable;
	}

	public void setSplitTable(boolean splitTable) {
		this.splitTable = splitTable;
	}

	public String getPriKeyDesc() {
		return fields.stream()
				.filter(FieldParam::isFieldIsKey)
				.map(FieldParam::getFieldName)
				.collect(Collectors.joining("` ,`", "`", "`"));
	}
}
