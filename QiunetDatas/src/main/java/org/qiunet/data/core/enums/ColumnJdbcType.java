package org.qiunet.data.core.enums;

/***
 * 数据库的字段类型
 * 指定几个即可
 *
 * qiunet
 * 2019-08-21 17:14
 ***/
public enum  ColumnJdbcType {
	/***
	 * int
	 */
	INT("INT"),
	/**
	 * long
	 */
	BIGINT("BIGINT"),
	/***
	 * 0 ~ 255 的文本
	 */
	VARCHAR255("VARCHAR(255"),
	/***
	 * 0 ~ 1000 的文本
	 */
	VARCHAR1000("VARCHAR(1000"),
	/**
	 * 0 ~ 65536 的文本
	 */
	TEXT("TEXT"),
	/**
	 * 0 ~ 1.6亿 的文本
	 */
	MEDIUMTEXT("MEDIUMTEXT"),
	/**
	 * 0 ~ 42亿的文本
	 */
	LONGTEXT("LONGTEXT"),
	;

	private String jdbcType;
	ColumnJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getJdbcType() {
		return jdbcType;
	}
}
