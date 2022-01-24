package org.qiunet.function.condition.doc;

/***
 *
 * @author qiunet
 * 2022/1/24 16:24
 */
public class AiActionDoc {
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String desc;

	public AiActionDoc() {
	}

	public AiActionDoc(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
