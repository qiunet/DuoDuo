package org.qiunet.function.reward;

import org.qiunet.cfg.manager.base.LoadSandbox;
import org.qiunet.cfg.resource.IResourceCfg0;
import org.qiunet.function.base.IResourceCfg;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.exceptions.CustomException;

import java.util.HashMap;
import java.util.Map;

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

	public RewardConfig(int cfgId, long count) {
		this.put("id", String.valueOf(cfgId));
		this.put("count", String.valueOf(count));
	}

	/**
	 * 转 rewardItem
	 * @return rewardItem 实例
	 */
	public BaseCfgReward convertToRewardItem() {
		IResourceCfg0 res = LoadSandbox.instance.getResById(getCfgId());
		if (res == null) {
			throw new CustomException("res {} null point exception", getCfgId());
		}
		return ((IResourceCfg) res).type().createRewardItem(this);
	}

	public int getCfgId() {
		return getInt("id");
	}

	public long getCount() {
		return getLong("count");
	}

	@Override
	public Map<Object, String> returnMap() {
		return this;
	}
}
