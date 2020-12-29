package org.qiunet.function.reward;

import com.google.common.collect.ImmutableList;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;

import java.util.List;

/***
 * 不可变更的奖励
 *
 * @author qiunet
 * 2020-12-28 20:35
 */
public class UnmodifiableRewards<Obj extends IThreadSafe & IPlayer> extends Rewards<Obj> {

	public UnmodifiableRewards(List<BaseReward<Obj>> list) {
		super(ImmutableList.copyOf(list));
	}

	public UnmodifiableRewards(String dbJsonString) {
		super(dbJsonString, true);
	}
}
