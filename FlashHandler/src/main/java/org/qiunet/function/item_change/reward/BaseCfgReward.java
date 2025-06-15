package org.qiunet.function.item_change.reward;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.ItemChangeConfig;
import org.qiunet.utils.thread.IThreadSafe;

/**
 * @author qiunet
 * 2023/12/22 11:41
 */
public abstract class BaseCfgReward<Obj extends IThreadSafe & IPlayer> extends BaseReward<Obj> {


	public BaseCfgReward(int id, long count) {
		super(id, count);
	}

	public BaseCfgReward(ItemChangeConfig itemChangeConfig) {
		this(itemChangeConfig.getCfgId(), itemChangeConfig.getCount());
	}

	/**
	 * 转RewardConfig
	 * @return
	 */
	public ItemChangeConfig toRewardConfig() {
		return new ItemChangeConfig(id, getCount());
	}
}
