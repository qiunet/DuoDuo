package project.init.elements.entity;

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
			case "Boolean":
			case "boolean":
				return null == defaultVal ? null : defaultVal;
			case "Date":
				if ("now".equals(defaultVal)) return "new Date()";
				return null == defaultVal ? "new Date(0)" : "new Date("+ DateUtil.stringToDate(defaultVal).getTime()+"L)";
		}
		return null;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getComment() {
		int commentStartIndex = 40;
		StringBuilder sb = new StringBuilder();
		for (int i = (name.length() + type.length() + 10); i < commentStartIndex; i++){
			sb.append(" ");
		}
		sb.append("/** ").append(comment).append("  */");
		return sb.toString();
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
