package org.qiunet.project.init.define;

import org.qiunet.utils.string.StringUtil;

/***
 *
 *
 * qiunet
 * 2019-08-14 20:34
 ***/
public class FieldDefine {

	private String name;

	private String type;

	private String comment;

	private String jdbcType;

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public boolean isJdbcTypeNotEmpty() {
		return !StringUtil.isEmpty(jdbcType);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
