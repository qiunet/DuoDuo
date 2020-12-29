package org.qiunet.function.base;

import org.qiunet.function.consume.BaseConsume;
import org.qiunet.function.consume.ConsumeConfig;
import org.qiunet.function.reward.BaseReward;
import org.qiunet.function.reward.RewardConfig;

/***
 * 资源的子类型枚举.
 *
 * @author qiunet
 * 2020-12-28 11:49
 */
public interface IResourceSubType {
	/**
	 * 根据子类型. 创建对应的消耗实例
	 * @param consumeConfig 消耗配置
	 * @param <T> 消耗实例泛型
	 * @return 消耗实例
	 */
	<T extends BaseConsume> T createConsume(ConsumeConfig consumeConfig);

	/**
	 * 根据子类型. 创建对应的奖励实例
	 * @param rewardConfig 奖励配置
	 * @param <T> 奖励的泛型类
	 * @return 奖励的实例
	 */
	<T extends BaseReward> T createRewardItem(RewardConfig rewardConfig);
}
