package org.qiunet.function.reward;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.base.IOperationType;
import org.qiunet.listener.event.IEventData;

/***
 * 获得奖励事件
 * 仅仅获得奖励本身
 *
 * @author qiunet
 * 2021-01-05 20:43
 */
public class GainRewardEventData implements IEventData {
	/**
	 * 玩家
	 */
	private IPlayer player;
	/**
	 * 奖励本身
	 */
	private Rewards rewards;
	/**
	 * 操作类型
	 */
	private IOperationType type;

	public static GainRewardEventData valueOf(Rewards rewards, IPlayer player, IOperationType type) {
		GainRewardEventData event = new GainRewardEventData();
		event.rewards = rewards;
		event.player = player;
		event.type = type;
		return event;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public Rewards getRewards() {
		return rewards;
	}

	public IOperationType getType() {
		return type;
	}
}
