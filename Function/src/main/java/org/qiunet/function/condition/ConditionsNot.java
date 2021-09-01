package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

/***
 * Condition 取反
 *
 * qiunet
 * 2021/8/10 11:09
 **/
public class ConditionsNot<Obj> implements IConditions<Obj> {
	private final IConditions<Obj> a;

	public ConditionsNot(IConditions<Obj> a) {
		this.a = a;
	}

	@Override
	public StatusResult verify(Obj obj) {
		return ! a.verify(obj).isSuccess()
				? StatusResult.SUCCESS : StatusResult.FAIL;

	}
}
