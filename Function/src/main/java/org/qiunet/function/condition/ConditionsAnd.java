package org.qiunet.function.condition;

/***
 * 两个Condition and
 *
 * qiunet
 * 2021/8/10 11:09
 **/
public class ConditionsAnd<Obj> implements IConditionData<Obj> {
	private final Conditions<Obj> a;
	private final Conditions<Obj> b;

	public ConditionsAnd(Conditions<Obj> a, Conditions<Obj> b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public ConditionResult verify(Obj obj) {
		return (a.verify(obj) == ConditionResult.SUCCESS)
				&& (b.verify(obj) == ConditionResult.SUCCESS)
				? ConditionResult.SUCCESS : ConditionResult.FAIL;

	}
}
