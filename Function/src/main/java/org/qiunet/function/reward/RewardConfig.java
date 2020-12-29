package org.qiunet.function.reward;

import org.qiunet.function.base.IResourceSubType;

import java.util.function.Function;

/***
 * 奖励的配置
 * 可能邮件什么发奖励. 也通过序列化成该对象的json string 然后发放.
 *
 *
 * @author qiunet
 * 2020-12-28 22:45
 */
public final class RewardConfig {
	/**
	 * 资源id
	 */
	private int cfgId;
	/**
	 * 数值
	 */
	private long count;
	/**
	 *  额外信息.
	 *  比如一些原始信息. 需要通过奖励同步的. 都记录这里.
	 */
	private String extraInfo;

	public RewardConfig() {
	}

	public RewardConfig(int cfgId, long count) {
		this.cfgId = cfgId;
		this.count = count;
	}

	public RewardConfig(int cfgId, long count, String extraInfo) {
		this.cfgId = cfgId;
		this.count = count;
		this.extraInfo = extraInfo;
	}

	/**
	 * 转 rewardItem
	 * @param subTypeGetter subType 获取
	 * @return rewardItem 实例
	 */
	public BaseReward convertToRewardItem(Function<Integer, IResourceSubType> subTypeGetter) {
		return subTypeGetter.apply(cfgId).createRewardItem(this);
	}

	public int getCfgId() {
		return cfgId;
	}

	public long getCount() {
		return count;
	}

	public String getExtraInfo() {
		return extraInfo;
	}
}
