package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

/***
 * 返回fail的condition
 *
 * qiunet
 * 2021/8/16 23:05
 **/
public final class AlwaysFailCondition<Obj> implements IConditions<Obj> {
	private static final AlwaysFailCondition<?> instance = new AlwaysFailCondition<>();

	private AlwaysFailCondition(){}

	public static <Obj> AlwaysFailCondition<Obj> getInstance() {
		return (AlwaysFailCondition<Obj>) instance;
	}

	@Override
	public StatusResult verify(Obj obj) {
		return StatusResult.FAIL;
	}
}
