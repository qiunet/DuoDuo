package org.qiunet.function.reward;

import com.google.common.collect.Lists;
import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.cfg.listener.CfgLoadCompleteEventData;
import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.utils.listener.event.EventListener;
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

		UnmodifiableRewards unmodifiableRewards = new UnmodifiableRewards(configList);
		rewardList.add(unmodifiableRewards);
		return unmodifiableRewards;
	}

	private static final List<Rewards> rewardList = Lists.newLinkedList();

	/**
	 * 清理数据
	 * @param data
	 */
	@EventListener
	public void cfgLoadOver(CfgLoadCompleteEventData data) {
		for (Rewards rewards : rewardList) {
			((IAfterLoad)rewards).afterLoad();
		}
		rewardList.clear();
	}
}
