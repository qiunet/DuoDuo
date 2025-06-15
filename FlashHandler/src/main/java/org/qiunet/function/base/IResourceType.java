package org.qiunet.function.base;

import org.qiunet.function.item_change.ItemChangeConfig;
import org.qiunet.function.item_change.consume.BaseCfgConsume;
import org.qiunet.function.item_change.reward.BaseCfgReward;

/***
 * 资源的类型枚举.
 *
 * @author qiunet
 * 2020-12-28 11:49
 */
public interface IResourceType {
	/**
	 *  枚举名
	 * @return 枚举名
	 */
	String name();
	/**
	 * 根据类型. 创建对应的消耗实例
	 * @param consumeConfig 消耗配置
	 * @param <T> 消耗实例泛型
	 * @return 消耗实例
	 */
	<T extends BaseCfgConsume<?>> T createConsumeItem(ItemChangeConfig consumeConfig);

	/**
	 * 根据类型. 创建对应的奖励实例
	 * @param itemChangeConfig 奖励配置
	 * @param <T> 奖励的泛型类
	 * @return 奖励的实例
	 */
	<T extends BaseCfgReward<?>> T createRewardItem(ItemChangeConfig itemChangeConfig);
}
