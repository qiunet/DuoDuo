package org.qiunet.function.reward;

import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.base.IOperationType;

/***
 *
 *
 * @author qiunet
 * 2020-12-28 20:37
 */
public abstract class RewardItem<Obj extends IThreadSafe & IPlayer> {
	/**
	 * 奖励id
	 */
	private int cfgId;
	/**
	 * 奖励数
	 */
	private long value;
	/**
	 * 校验
	 * @param player 玩家对象
	 * @param type 操作类型
	 * @return 结果.
	 */
	abstract RewardResult verify(Obj player, IOperationType type);

	/**
	 * 发放奖励
	 * @param context 奖励上下文
	 */
	abstract void grant(RewardContext<Obj> context);

	/**
	 * 复制一份
	 * @return
	 */
	public RewardItem<Obj> copy() {
		return copy(1);
	}

	/**
	 * 复制指定倍率的Item
	 * @param multi
	 * @return
	 */
	public abstract RewardItem<Obj> copy(int multi);

	/**
	 * 转RewardConfig
	 * @return
	 */
	public RewardConfig toRewardConfig() {
		return new RewardConfig(cfgId, value);
	}
}
