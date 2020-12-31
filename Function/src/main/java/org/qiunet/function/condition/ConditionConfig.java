package org.qiunet.function.condition;

/***
 * 条件配置
 * 一般是json格式.
 * [{"type": "PLAYER_MIN_LEVEL", "value": "30"}, {"type": "PLAYER_MAX_LEVEL", "value": "180"}]
 * 表示 最小30级, 最大180级.
 * @author qiunet
 * 2020-12-30 20:56
 */
public class ConditionConfig {
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 值
	 */
	private String value;


	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
}
