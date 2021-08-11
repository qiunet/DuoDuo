package org.qiunet.function.condition;

import java.util.List;
import java.util.function.Consumer;

/***
 * 对Obj 的条件校验.
 *
 * @Author qiunet
 * @Date 2020/12/28 07:47
 **/
public class Conditions<Obj> implements IConditions<Obj> {
	/**
	 * 所有条件
	 */
	private final List<ICondition<Obj, ?>> conditions;

	Conditions(List<ICondition<Obj, ?>> conditions) {
		this.conditions = conditions;
	}

	/**
	 *  校验先有条件
	 * @param obj
	 * @return
	 */
	@Override
	public ConditionResult verify(Obj obj) {
		for (ICondition<Obj, ?> condition : conditions) {
			ConditionResult result = condition.verify(obj);
			if (result.isFail()) {
				return result;
			}
		}
		return ConditionResult.SUCCESS;
	}

	/**
	 * 循环先有条件
	 * @param consumer 消费者
	 */
	public void forEach(Consumer<ICondition<Obj, ?>> consumer) {
		conditions.forEach(consumer);
	}
}
