package org.qiunet.game.tests.client.data.condition.base;

import org.qiunet.function.condition.IConditionType;

/***
 *
 *
 * qiunet
 * 2021/8/11 09:25
 **/
public enum ConditionType implements IConditionType {
	/**
	 * 是否登录
	 */
	LOGIN,
	/**
	 * 是否注册
	 */
	REGISTER_COUNT,
	/**
	 * 随机名称是否已经准备
	 */
	RANDOM_NAME_PRESENT,
	/**
	 * 鉴权
	 */
	AUTH,
}
