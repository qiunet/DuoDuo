package org.qiunet.test.function.test.ai.condition;

import org.qiunet.function.condition.IConditionType;

/***
 *
 * @author qiunet
 * 2021/12/13 15:33
 */
public enum ConditionType implements IConditionType {

	SEE_GOBLIN("看到哥布林"),

	SEE_OMA("看见半兽人"),
	;
	private String desc;

	ConditionType(String desc) {
		this.desc = desc;
	}

	@Override
	public String desc() {
		return desc;
	}
}
