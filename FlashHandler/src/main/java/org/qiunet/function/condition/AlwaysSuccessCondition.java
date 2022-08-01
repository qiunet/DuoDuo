package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

/***
 * 返回success的condition
 *
 * qiunet
 * 2021/8/16 23:05
 **/
public final class AlwaysSuccessCondition<Obj> implements IConditions<Obj> {
	private static final AlwaysSuccessCondition<?> instance = new AlwaysSuccessCondition<>();

	private AlwaysSuccessCondition(){}

	public static <Obj> AlwaysSuccessCondition<Obj> getInstance() {
		return (AlwaysSuccessCondition<Obj>) instance;
	}

	@Override
	public StatusResult verify(Obj obj) {
		return StatusResult.SUCCESS;
	}
}
