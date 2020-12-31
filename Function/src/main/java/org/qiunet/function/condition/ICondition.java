package org.qiunet.function.condition;

/***
 *
 *
 * @author qiunet
 * 2020-12-30 16:11
 */
public interface ICondition<Obj, Type extends Enum<Type> & IConditionType> {
	/**
	 * 获得type
	 * @return
	 */
	Type getType();
	/**
	 * 校验 条件是否满足
	 * @param obj 主体对象
	 * @return
	 */
	ConditionResult verify(Obj obj);
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
		default ConditionResult verify(Void unused) {
			return verify();
		}

		ConditionResult verify();
	}
}


