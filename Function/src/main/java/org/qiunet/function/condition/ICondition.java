package org.qiunet.function.condition;

import org.qiunet.flash.handler.context.status.StatusResult;

/***
 *
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

	/**
	 * 初始化
	 * @param config 配置
	 */
	void init(ConditionConfig config);
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
}


