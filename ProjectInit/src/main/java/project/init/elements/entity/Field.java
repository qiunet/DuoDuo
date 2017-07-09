package project.init.elements.entity;

/**
 * @author qiunet
 *         Created on 16/11/21 13:19.
 */
public class Field {
	private String name;
	private String type;
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
