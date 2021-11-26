package org.qiunet.function.condition;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.context.status.StatusResult;

import java.util.Arrays;
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

	public Conditions(ICondition<Obj, ?> ... conditions) {
		this(Lists.newArrayList(conditions));
	}

	public Conditions(List<ICondition<Obj, ?>> conditions) {
		this.conditions = conditions;
	}

	/**
	 *  校验先有条件
	 * @param obj
	 * @return
	 */
	@Override
	public StatusResult verify(Obj obj) {
		for (ICondition<Obj, ?> condition : conditions) {
			StatusResult result = condition.verify(obj);
			if (result.isFail()) {
				return result;
			}
		}
		return StatusResult.SUCCESS;
	}

	/**
	 * 循环先有条件
	 * @param consumer 消费者
	 */
	public void forEach(Consumer<ICondition<Obj, ?>> consumer) {
		conditions.forEach(consumer);
	}

	/**
	 * 添加条件.
	 * 如果是不可变的Conditions, 会抛出异常
	 * @param conditions 条件
	 * @param <Type> 条件类型
	 */
	public <Type extends Enum<Type> &IConditionType> Conditions<Obj> addConditions(ICondition<Obj, Type> ... conditions) {
		this.conditions.addAll(Arrays.asList(conditions));
		return this;
	}
}
