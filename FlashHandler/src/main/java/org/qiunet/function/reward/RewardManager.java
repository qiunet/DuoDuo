package org.qiunet.function.reward;

import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.utils.scanner.anno.AutoWired;

import java.util.Collections;
import java.util.List;

/***
 * 奖励的管理类
 * @Author qiunet
 * @Date 2020/12/29 07:51
 **/
public enum RewardManager {
	instance;


	@AutoWired
	private static IBasicFunction resourceManager;
	/**
	 * 空的 rewards
	 */
	public static final Rewards EMPTY_REWARDS = new UnmodifiableRewards(Collections.emptyList());
	/**
	 * 创建不可修改的 Rewards
	 * @param configList 配置列表
	 * @return Consumes
	 */
	public Rewards createRewards(List<RewardConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return EMPTY_REWARDS;
		}

		return new UnmodifiableRewards(configList);
	}
}
