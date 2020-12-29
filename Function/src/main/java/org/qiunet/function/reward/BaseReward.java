package org.qiunet.function.reward;

import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.base.IOperationType;

/***
 * 奖励的基础类
 *
 * @author qiunet
 * 2020-12-28 20:37
 */
public abstract class BaseReward<Obj extends IThreadSafe & IPlayer> {
	/**
	 * 奖励id
	 */
	private final int cfgId;
	/**
	 * 奖励数
	 */
	private final long value;

	public BaseReward(int cfgId, long value) {
		this.cfgId = cfgId;
		this.value = value;
	}

	public BaseReward(RewardConfig rewardConfig) {
		this.cfgId = rewardConfig.getCfgId();
		this.value = rewardConfig.getValue();
	}

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
	public BaseReward<Obj> copy() {
		return copy(1);
	}

	/**
	 * 复制指定倍率的Item
	 * @param multi
	 * @return
	 */
	public abstract BaseReward<Obj> copy(int multi);

	/**
	 * 转RewardConfig
	 * @return
	 */
	public RewardConfig toRewardConfig() {
		return new RewardConfig(cfgId, value);
	}
}
