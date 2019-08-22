package org.qiunet.entity2table.command;

/**
 * 用于存放创建表的字段信息
 */
public class CreateTableParam {

	/**
	 * 字段名
	 */
	private String fieldName;

	/**
	 * 字段类型
	 */
	private String fieldType;

	/**
	 * 字段是否非空
	 */
	private boolean	fieldIsNull;

	/**
	 * 字段是否是主键
	 */
	private boolean	fieldIsKey;

	/**
	 * 主键是否自增
	 */
	private boolean fieldIsAutoIncrement;

	/**
	 * 字段默认值
	 */
	private String fieldDefaultValue;
	/**
	 * 注释
	 */
	private String fieldComment;

	public String getFieldComment() {
		return fieldComment;
	}

	public void setFieldComment(String fieldComment) {
		this.fieldComment = fieldComment;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isFieldIsNull() {
		return fieldIsNull;
	}

	public void setFieldIsNull(boolean fieldIsNull) {
		this.fieldIsNull = fieldIsNull;
	}

	public boolean isFieldIsKey() {
		return fieldIsKey;
	}

	public void setFieldIsKey(boolean fieldIsKey) {
		this.fieldIsKey = fieldIsKey;
	}

	public boolean isFieldIsAutoIncrement() {
		return fieldIsAutoIncrement;
	}

	public void setFieldIsAutoIncrement(boolean fieldIsAutoIncrement) {
		this.fieldIsAutoIncrement = fieldIsAutoIncrement;
	}

	public String getFieldDefaultValue() {
		return fieldDefaultValue;
	}

	public void setFieldDefaultValue(String fieldDefaultValue) {
		this.fieldDefaultValue = fieldDefaultValue;
	}
}
