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

	private String dbSource;

	private List<FieldParam> fields;

	public TableCreateParam(String tableName, String comment, List<FieldParam> fields, boolean splitTable, String dbSource) {
		this.splitTable = splitTable;
		this.tableName = tableName;
		this.dbSource = dbSource;
		this.comment = comment;
		this.fields = fields;
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
