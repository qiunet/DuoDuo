package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

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
	public StatusResult verify(Obj obj) {
		return (a.verify(obj) == StatusResult.SUCCESS)
				|| (b.verify(obj) == StatusResult.SUCCESS)
				? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
