package org.qiunet.function.condition.doc;

import java.util.List;

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
	/**
	 * 需要配置的参数
	 */
	private List<AiActionParam> params;

	public AiActionDoc() {
	}

	public AiActionDoc(String name, String desc, List<AiActionParam> params) {
		this.name = name;
		this.desc = desc;
		this.params = params;
	}

	public List<AiActionParam> getParams() {
		return params;
	}

	public void setParams(List<AiActionParam> params) {
		this.params = params;
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
