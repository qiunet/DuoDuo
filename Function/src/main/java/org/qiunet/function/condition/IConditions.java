package org.qiunet.function.condition;

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
	ConditionResult verify(Obj obj);
}
