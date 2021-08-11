package org.qiunet.function.condition;

/***
 * 两个Condition or
 *
 * qiunet
 * 2021/8/10 11:09
 **/
public class ConditionsOr<Obj> implements IConditions<Obj> {
	private final Conditions<Obj> a;
	private final Conditions<Obj> b;

	public ConditionsOr(Conditions<Obj> a, Conditions<Obj> b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public ConditionResult verify(Obj obj) {
		return (a.verify(obj) == ConditionResult.SUCCESS)
				|| (b.verify(obj) == ConditionResult.SUCCESS)
				? ConditionResult.SUCCESS : ConditionResult.FAIL;
	}
}
