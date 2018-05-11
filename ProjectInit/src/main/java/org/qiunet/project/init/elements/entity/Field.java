package org.qiunet.project.init.elements.entity;

import org.qiunet.utils.date.DateUtil;

import java.text.ParseException;

/**
 * @author qiunet
 *         Created on 16/11/21 13:19.
 */
public class Field {
	private String name;
	private String type;
	private String defaultVal;
	private String comment;

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

	public String getDefaultVal() throws ParseException {
		switch (type) {
			case "String":
				return null == defaultVal ? "\"\"" : "\""+defaultVal+"\"";
			case "int":
			case "long":
				return null == defaultVal ? null : defaultVal;
			case "boolean":
			case "Boolean":
				return null == defaultVal ? null : defaultVal;
			case "Date":
				if ("now".equals(defaultVal)) return "new Date()";
				return null == defaultVal ? "new Date(0)" : "new Date("+ DateUtil.stringToDate(defaultVal).getTime()+"L)";
			default:
				throw new IllegalArgumentException("not support type for ["+type+"]");
		}
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFieldDesc() throws ParseException {
		StringBuilder sb = new StringBuilder();
		sb.append(type).append(" ").append(name);
		String defaultVal = getDefaultVal();
		if (defaultVal != null) sb.append(" = ").append(defaultVal);
		sb.append(";");
		for (int i = sb.length(); i < 35; i++) sb.append(" ");
		sb.append("/*** ").append(comment);
		for (int i = sb.length(); i < 50; i++) sb.append(" ");
		sb.append("**/");
		return sb.toString();
	}
}
