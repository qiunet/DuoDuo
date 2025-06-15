package org.qiunet.function.item_change.reward;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.AbstractItemChange;
import org.qiunet.function.item_change.ItemChangeVerifyParams;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.ArrayList;
import java.util.List;

/***
 * 奖励
 *
 * @author qiunet
 * 2020-12-28 20:35
 */
public class Rewards<Obj extends IThreadSafe & IPlayer> extends
	AbstractItemChange<Rewards<Obj>, Obj, RewardContext<Obj>, BaseReward<Obj>> {

	public Rewards() {
		this(new ArrayList<>(5));
	}

	public Rewards(List<BaseReward<Obj>>  rewardList) {
		super(rewardList);
	}

	@Override
	protected Rewards<Obj> newInstance() {
		return new Rewards<>();
	}

	@Override
	protected RewardContext<Obj> newContext(ItemChangeVerifyParams<Obj> params) {
		return new RewardContext<>(params, this);
	}
}
