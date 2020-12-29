package org.qiunet.function.reward;

import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.utils.exceptions.CustomException;

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
	 * @param context 上下文
	 * @return 结果.
	 */
	abstract RewardResult doVerify(RewardContext<Obj> context);
	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果.
	 */
	final RewardResult verify(RewardContext<Obj> context) {
		if (context.getMulti() < 1) {
			throw new CustomException("Multi 数值 {} 不合法, 必须 >= 1", context.getMulti());
		}

		if (value * context.getMulti() < 0) {
			throw new CustomException("Value {} 和 multi {} 相乘后会溢出!", value, context.getMulti());
		}

		return doVerify(context);
	}
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
