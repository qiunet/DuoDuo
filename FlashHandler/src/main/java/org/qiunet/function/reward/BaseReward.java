package org.qiunet.function.reward;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.Objects;

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

	/**
	 * 奖励id
	 */
	protected final Object id;
	/**
	 * 奖励数
	 */
	protected long count;

	public BaseReward(Object id, long count) {
		Preconditions.checkState(count > 0, "count can not less than 1");
		this.count = count;
		this.id = id;

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

		if (count < 0) {
			throw new CustomException("Count 小于 0!", count);
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
		if (multi * count < 0) {
			throw new CustomException("Count {} 和 multi {} 相乘数值溢出!", count, multi);
		}
		return doCopy(multi);
	}

	protected abstract BaseReward<Obj> doCopy(int multi);

	/**
	 * 转RewardConfig
	 * @return
	 */
	public abstract RewardConfig toRewardConfig();

	public Object getId() {
		return id;
	}

	public long getCount() {
		return count;
	}
	/**
	 * 是否可以合并
	 * @param reward 奖励的具体对象
	 * @return 是否可以合并
	 */
	public boolean canMerge(BaseReward<Obj> reward) {
		return this.getClass() == reward.getClass()
				&& Objects.equals(getId(), reward.getId());
	}
	/**
	 * 合并一个reward
	 * @param reward
	 */
	public void doMerge(BaseReward<Obj> reward) {
		this.count += reward.count;
	}
}
