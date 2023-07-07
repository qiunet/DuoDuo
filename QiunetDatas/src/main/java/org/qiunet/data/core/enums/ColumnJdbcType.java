package org.qiunet.data.core.enums;

import org.qiunet.data.core.support.db.Column;

import java.lang.reflect.Field;

/***
 * 数据库的字段类型
 * 指定几个即可
 *
 * qiunet
 * 2019-08-21 17:14
 ***/
public enum  ColumnJdbcType {
	NULL("", 0),
	/***
	 * int
	 */
	INT("INT", 101),
	/**
	 * long
	 */
	BIGINT("BIGINT", 102),
	/***
	 * 0 ~ 255 的文本
	 */
	VARCHAR190("VARCHAR(190)", 200),
	/***
	 * 0 ~ 255 的文本
	 */
	VARCHAR255("VARCHAR(255)", 201),
	/***
	 * 0 ~ 1000 的文本
	 */
	VARCHAR1000("VARCHAR(1000)", 202),
	/**
	 * 0 ~ 65536 的文本
	 */
	TEXT("TEXT", 203),
	/**
	 * 0 ~ 1.6亿 的文本
	 */
	MEDIUMTEXT("MEDIUMTEXT", 204),
	/**
	 * 0 ~ 42亿的文本
	 */
	LONGTEXT("LONGTEXT", 205),
	/***
	 * boolean
	 */
	BOOLEAN("TINYINT", 301),
	;
	private final int factor;
	private final String jdbcType;
	ColumnJdbcType(String jdbcType, int factor) {
		this.jdbcType = jdbcType;
		this.factor = factor;
	}

	public String getJdbcTypeDesc() {
		return jdbcType;
	}

	/**
	 * 判断是否可以修改类型
	 * 同类型, 并且只能往上修改
	 * @param jdbcType
	 * @return
	 */
	public boolean canAlterTo(ColumnJdbcType jdbcType) {
		return this.factor / 100 == jdbcType.factor/100
			&& this.factor  < jdbcType.factor;
	}

	public static ColumnJdbcType parse(Class type, Column column, Field field) {
		ColumnJdbcType jdbcType = column.jdbcType();
		boolean key = column.isKey();

		if (type == Integer.class || type == int.class)
			return INT;
		if (type == Long.class || type == long.class)
			return BIGINT;
		if (type == Boolean.class || type == boolean.class)
			return BOOLEAN;
		 if (type == String.class) {
			if (jdbcType == NULL) {
				if (key) return VARCHAR190;
				return VARCHAR255;
			}

			if (key && jdbcType != VARCHAR190) {
				throw new IllegalArgumentException("Can set jdbcType [" + jdbcType + "] to String PRIMARY KEY ["+field.getDeclaringClass().getName()+"#"+field.getName()+"]!");
			}

			if (jdbcType == INT || jdbcType == BIGINT)
				throw new IllegalArgumentException("Can set jdbcType [" + jdbcType + "] to String Column");
			return jdbcType;
		}
		throw new IllegalArgumentException("Not support type for ["+type+"]");
	}

	public static ColumnJdbcType parse(String columnType) {
		columnType = columnType.toLowerCase();
		if (columnType.startsWith("int")) return INT;
		if (columnType.startsWith("bigint")) return BIGINT;
		if (columnType.startsWith("tinyint")) return BOOLEAN;
		if (columnType.equals("varchar(190)")) return VARCHAR190;
		if (columnType.equals("varchar(255)")) return VARCHAR255;
		if (columnType.equals("varchar(1000)")) return VARCHAR1000;
		if (columnType.equals("text")) return TEXT;
		if (columnType.equals("mediumtext")) return MEDIUMTEXT;
		if (columnType.equals("longtext")) return LONGTEXT;
		throw new IllegalArgumentException("Not support type for ["+columnType+"]");
	}
}
