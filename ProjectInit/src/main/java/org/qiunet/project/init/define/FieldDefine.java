package org.qiunet.project.init.define;

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

	private int length;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
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
