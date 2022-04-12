package org.qiunet.function.reward;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.base.IResourceType;
import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.anno.AutoWired;

/***
 * 奖励的基础类
 *
 * @author qiunet
 * 2020-12-28 20:37
 */
public abstract class BaseReward<Obj extends IThreadSafe & IPlayer> {
	/**
	 * 需要格子总和
	 */
	protected static final ArgumentKey<Integer> needGridSum = new ArgumentKey<>();

	@AutoWired
	private static IBasicFunction basicFunction;

	/**
	 * 奖励id
	 */
	protected final String cfgId;
	/**
	 * 奖励数
	 */
	protected long value;

	public BaseReward(String cfgId, long value) {
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

		if (value < 0) {
			throw new CustomException("Value 小于 0!", value);
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
	public final BaseReward<Obj> copy() {
		return copy(1);
	}

	/**
	 * 复制指定倍率的Item
	 * @param multi
	 * @return
	 */
	public final BaseReward<Obj> copy(int multi) {
		if (multi * value < 0) {
			throw new CustomException("value {} 和 multi {} 相乘数值溢出!", value, multi);
		}
		return doCopy(multi);
	}

	protected abstract BaseReward<Obj> doCopy(int multi);

	/**
	 * 转RewardConfig
	 * @return
	 */
	public RewardConfig toRewardConfig() {
		return new RewardConfig(cfgId, value);
	}

	public String getCfgId() {
		return cfgId;
	}

	public long getValue() {
		return value;
	}
	/**
	 * 是否可以合并
	 * @param reward 奖励的具体对象
	 * @return 是否可以合并
	 */
	public boolean canMerge(BaseReward<Obj> reward) {
		return this.getClass() == reward.getClass()
				&& this.getCfgId() == reward.getCfgId();
	}
	/**
	 * 合并一个reward
	 * @param reward
	 */
	public void doMerge(BaseReward<Obj> reward) {
		this.value += reward.value;
	}
	/**
	 * 获得type
	 * @return
	 */
	public <Type extends Enum<Type> & IResourceType> Type resType() {
		return basicFunction.getResType(cfgId);
	}
}
