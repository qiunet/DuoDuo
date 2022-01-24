package org.qiunet.function.condition.doc;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/1/24 16:23
 */
public class AiConfigDoc {
	/**
	 * 所有的条件相关的
	 */
	private List<ConditionDoc> conditionDocs;
	/**
	 * 所有action Node doc 描述
	 */
	private List<AiActionDoc> actionDocs;

	public AiConfigDoc() {}

	public AiConfigDoc(List<ConditionDoc> conditionDocs, List<AiActionDoc> actionDocs) {
		this.conditionDocs = conditionDocs;
		this.actionDocs = actionDocs;
	}

	public List<ConditionDoc> getConditionDocs() {
		return conditionDocs;
	}

	public void setConditionDocs(List<ConditionDoc> conditionDocs) {
		this.conditionDocs = conditionDocs;
	}

	public List<AiActionDoc> getActionDocs() {
		return actionDocs;
	}

	public void setActionDocs(List<AiActionDoc> actionDocs) {
		this.actionDocs = actionDocs;
	}
}
