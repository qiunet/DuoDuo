package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

/***
 * 两个Condition and
 *
 * qiunet
 * 2021/8/10 11:09
 **/
public class ConditionsAnd<Obj> implements IConditions<Obj> {
	private final IConditions<Obj> a;
	private final IConditions<Obj> b;

	public ConditionsAnd(IConditions<Obj> a, IConditions<Obj> b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public StatusResult verify(Obj obj) {
		return (a.verify(obj) == StatusResult.SUCCESS)
				&& (b.verify(obj) == StatusResult.SUCCESS)
				? StatusResult.SUCCESS : StatusResult.FAIL;

	}
}
