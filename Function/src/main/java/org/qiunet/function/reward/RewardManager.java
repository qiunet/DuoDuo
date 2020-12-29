package org.qiunet.function.reward;

import org.qiunet.function.base.IResourceManager;
import org.qiunet.utils.scanner.anno.AutoWired;
import org.qiunet.utils.string.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 奖励的管理类
 * @Author qiunet
 * @Date 2020/12/29 07:51
 **/
public enum RewardManager {
	instance;


	@AutoWired
	private static IResourceManager resourceManager;
	/**
	 * 空的 rewards
	 */
	public static final Rewards EMPTY_CONSUMES = new UnmodifiableRewards(Collections.emptyList());
	/**
	 * 创建不可修改的 Rewards
	 * @param configList 配置列表
	 * @return Consumes
	 */
	public Rewards createRewards(List<RewardConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return EMPTY_CONSUMES;
		}

		List<BaseReward> list = configList.stream().map(cfg -> {
			return cfg.convertToRewardItem(id -> resourceManager.getResSubType(cfg.getCfgId()));
		}).collect(Collectors.toList());
		return new UnmodifiableRewards(list);
	}

	/**
	 * 创建不可修改的 Rewards
	 * @param dbJsonString 配置
	 * @return Consumes
	 */
	public Rewards createRewards(String dbJsonString) {
		if (StringUtil.isEmpty(dbJsonString)) {
			return EMPTY_CONSUMES;
		}
		return new UnmodifiableRewards(dbJsonString);
	}
}
