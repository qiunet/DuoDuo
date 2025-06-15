package org.qiunet.function.item_change.reward;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.ItemChangeConfig;
import org.qiunet.function.item_change.ItemChangeElement;
import org.qiunet.utils.thread.IThreadSafe;

/***
 * 奖励的基础类
 *
 * @author qiunet
 * 2020-12-28 20:37
 */
public abstract class BaseReward<Obj extends IThreadSafe & IPlayer> extends ItemChangeElement<BaseReward<Obj>, RewardContext<Obj>> {

	public BaseReward(int id, long count) {
		super(id, count);
	}
	/**
	 * 转RewardConfig
	 * @return
	 */
	public abstract ItemChangeConfig toRewardConfig();
}
