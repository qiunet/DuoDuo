package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

/***
 * 条件数据
 *
 * qiunet
 * 2021/8/10 11:06
 **/
public interface IConditions<Obj> {
	/**
	 * 校验 条件是否满足
	 * @param obj 主体对象
	 * @return
	 */
	StatusResult verify(Obj obj);

	/**
	 * 将结果 取反
	 * @return 取反后的对象
	 */
	default IConditions<Obj> invert() {
		return new ConditionsInvert<>(this);
	}

	/**
	 * 当前condition 和 指定condition 取或
	 * @param condition 指定的condition
	 * @return 返回的结果
	 */
	default IConditions<Obj> or(IConditions<Obj> condition){
		return new ConditionsOr<>(this, condition);
	}
	/**
	 * 当前condition 和 指定condition 取与
	 * @param condition 指定的condition
	 * @return 返回的结果
	 */
	default IConditions<Obj> and(IConditions<Obj> condition){
		return new ConditionsAnd<>(this, condition);
	}
}
