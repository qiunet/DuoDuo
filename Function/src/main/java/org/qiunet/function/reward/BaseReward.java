package org.qiunet.function.reward;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.base.IResourceType;
import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.anno.AutoWired;

/***
 * 奖励的基础类
 *
 * @author qiunet
 * 2020-12-28 20:37
 */
public abstract class BaseReward<Obj extends IThreadSafe & IPlayer> {
	@AutoWired
	private static IBasicFunction basicFunction;

	/**
	 * 奖励id
	 */
	protected final int cfgId;
	/**
	 * 奖励数
	 */
	protected final long value;

	public BaseReward(int cfgId, long value) {
		this.cfgId = cfgId;
		this.value = value;

		Preconditions.checkState(value > 0, "value can not less than 1");
	}

	public BaseReward(RewardConfig rewardConfig) {
		this(rewardConfig.getCfgId(), rewardConfig.getValue());
	}

	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果.
	 */
	public abstract StatusResult doVerify(RewardContext<Obj> context);
	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果.
	 */
	final StatusResult verify(RewardContext<Obj> context) {
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
	public abstract void grant(RewardContext<Obj> context);

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

	public int getCfgId() {
		return cfgId;
	}

	public long getValue() {
		return value;
	}

	/**
	 * 获得type
	 * @return
	 */
	public <Type extends Enum<Type> & IResourceType> Type resType() {
		return basicFunction.getResType(cfgId);
	}
}
