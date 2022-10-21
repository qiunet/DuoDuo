package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

/***
 * 单个ICondition 的not 包装
 *
 * @author qiunet
 * 2021/12/13 11:19
 */
public final class ConditionNot<Obj, Type extends Enum<Type> & IConditionType> implements ICondition<Obj, Type> {

	private final ICondition<Obj, Type> condition;

	public ConditionNot(ICondition<Obj, Type> condition) {
		this.condition = condition;
	}

	@Override
	public Type getType() {
		return condition.getType();
	}

	@Override
	public StatusResult verify(Obj obj) {
		return condition.verify(obj).isSuccess() ? StatusResult.FAIL : StatusResult.SUCCESS;
	}
}
