package org.qiunet.function.reward;

import org.qiunet.function.base.IResourceType;
import org.qiunet.utils.data.IKeyValueData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/***
 * 奖励的配置
 * 可能邮件什么发奖励. 也通过序列化成该对象的json string 然后发放.
 *
 * @author qiunet
 * 2020-12-28 22:45
 */
public final class RewardConfig extends HashMap<Object, String> implements IKeyValueData<Object, String> {

	public RewardConfig() {
	}

	public RewardConfig(int cfgId, long value) {
		this.put("cfgId", String.valueOf(cfgId));
		this.put("value", String.valueOf(value));
	}

	/**
	 * 转 rewardItem
	 * @param subTypeGetter subType 获取
	 * @return rewardItem 实例
	 */
	public BaseReward convertToRewardItem(Function<Integer, IResourceType> subTypeGetter) {
		return subTypeGetter.apply(getCfgId()).createRewardItem(this);
	}

	public int getCfgId() {
		return getInt("cfgId");
	}

	public long getValue() {
		return getLong("value");
	}

	@Override
	public Map<Object, String> returnMap() {
		return this;
	}
}
