package org.qiunet.function.condition;

/***
 * Condition 取反
 *
 * qiunet
 * 2021/8/10 11:09
 **/
public class ConditionsNot<Obj> implements IConditionCheck<Obj> {
	private final Conditions<Obj> a;

	public ConditionsNot(Conditions<Obj> a) {
		this.a = a;
	}

	@Override
	public ConditionResult verify(Obj obj) {
		return ! a.verify(obj).isSuccess()
				? ConditionResult.SUCCESS : ConditionResult.FAIL;

	}
}
