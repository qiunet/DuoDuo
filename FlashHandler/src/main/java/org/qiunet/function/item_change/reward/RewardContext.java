package org.qiunet.function.item_change.reward;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.ItemChangeContext;
import org.qiunet.function.item_change.ItemChangeVerifyParams;
import org.qiunet.utils.thread.IThreadSafe;

/***
 * 奖励上下文
 *
 * @author qiunet
 * 2020-12-28 20:45
 */
public class RewardContext<Obj extends IThreadSafe & IPlayer> extends ItemChangeContext<Obj> {

	RewardContext(ItemChangeVerifyParams<Obj> params, Rewards<Obj> rewards) {
		super(params, rewards);
	}

	public Rewards<Obj> getRewards() {
		return (Rewards<Obj>) items;
	}

	@Override
	protected void triggerEvent() {
		GainRewardEvent.valueOf(this).fireEventHandler();
	}
}
