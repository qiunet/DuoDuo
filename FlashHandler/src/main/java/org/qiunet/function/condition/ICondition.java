package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.utils.json.JsonUtil;

/***
 * 需要注入值的字段.
 * 使用{@link ConditionField 注解}
 *
 * 字段不要命名为 not  or. 这些为关键字.
 *
 * @author qiunet
 * 2020-12-30 16:11
 */
public interface ICondition<Obj, Type extends Enum<Type> & IConditionType> extends IConditions<Obj> {
	/**
	 * 获得type
	 * @return
	 */
	Type getType();
	/***
	 * 公共的条件 父类.
	 * 比如开服天数  条件等
	 *
	 * @author qiunet
	 * 2020-12-30 16:11
	 */
	interface PubCondition<Type extends Enum<Type> & IConditionType>
			extends ICondition<Void, Type> {
		@Override
		default StatusResult verify(Void unused) {
			return verify();
		}

		StatusResult verify();
	}

	@Override
	default ConditionNot<Obj, Type> not() {
		return new ConditionNot<>(this);
	}

	/**
	 * 转换成config
	 * @return
	 */
	default ConditionConfig conditionConfig() {
		ConditionConfig conditionConfig = new ConditionConfig(JsonUtil.toJsonObject(this));
		conditionConfig.setType(getType().name());
		return conditionConfig;
	}
}


