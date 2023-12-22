package org.qiunet.function.reward;

import org.qiunet.cfg.manager.base.LoadSandbox;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.base.IResourceCfg;
import org.qiunet.function.base.IResourceType;
import org.qiunet.utils.thread.IThreadSafe;

/**
 * @author qiunet
 * 2023/12/22 11:41
 */
public abstract class BaseCfgReward<Obj extends IThreadSafe & IPlayer> extends BaseReward<Obj> {


	public BaseCfgReward(Integer id, long count) {
		super(id, count);
	}

	public BaseCfgReward(RewardConfig rewardConfig) {
		this(rewardConfig.getCfgId(), rewardConfig.getCount());
	}

	@Override
	public Integer getId() {
		return (Integer) super.getId();
	}
	/**
	 * 转RewardConfig
	 * @return
	 */
	public RewardConfig toRewardConfig() {
		return new RewardConfig((Integer) id, count);
	}

	/**
	 * 获得type
	 * @return
	 */
	public <Type extends Enum<Type> & IResourceType> Type resType() {
		IResourceCfg res = LoadSandbox.instance.getResById((Integer) id);
		assert res != null;
		return res.type();
	}
}
