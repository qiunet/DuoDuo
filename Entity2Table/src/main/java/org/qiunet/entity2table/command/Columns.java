package org.qiunet.entity2table.command;

import org.qiunet.data.core.enums.ColumnJdbcType;

/**
 * 用于查询表中字段结构详细信息,其实是用于mybatis查询语句映射的对象
 */
public class Columns {

	/**
	 * 库名
	 */
	private String table_schema;
	/**
	 * 表名
	 */
	private String table_name;
	/**
	 * 字段名
	 */
	private String column_name;
	/**
	/**
	 * 字段默认值
	 */
	private String column_default;
	/**
	 * 是否可以为null
	 */
	private String is_nullable;
	/**
	 * 类型加长度拼接的字符串，例如varchar(100)
	 */
	private String column_type;
	/**
	 * 是否为自动增长，是的话为auto_increment
	 */
	private String extra;

	public String getTable_schema() {
		return table_schema;
	}

	public void setTable_schema(String table_schema) {
		this.table_schema = table_schema;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getColumn_default() {
		return column_default;
	}

	public void setColumn_default(String column_default) {
		this.column_default = column_default;
	}

	public String getIs_nullable() {
		return is_nullable;
	}

	public void setIs_nullable(String is_nullable) {
		this.is_nullable = is_nullable;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getColumn_type() {
		return column_type;
	}

	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}

	public ColumnJdbcType getJdbcType() {
		return ColumnJdbcType.parse(this.getColumn_type());
	}
}
