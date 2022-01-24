package org.qiunet.function.condition.doc;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/1/24 16:21
 */
public class ConditionDoc {
	/**
	 * 条件类型
	 */
	private String type;
	/**
	 * 条件描述
	 */
	private String desc;
	/**
	 * 条件的参数描述
	 */
	private List<ConditionParamDoc> paramDoc;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ConditionParamDoc> getParamDoc() {
		return paramDoc;
	}

	public void setParamDoc(List<ConditionParamDoc> paramDoc) {
		this.paramDoc = paramDoc;
	}
}
