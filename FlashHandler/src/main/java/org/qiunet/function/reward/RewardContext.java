package org.qiunet.function.reward;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.base.IOperationType;

import java.util.List;

/***
 * 奖励上下文
 *
 * @author qiunet
 * 2020-12-28 20:45
 */
public class RewardContext<Obj extends IThreadSafe & IPlayer> {
	/**
	 * 倍数
	 */
	private int multi;
	/**
	 * 玩家主体
	 */
	private Obj player;
	/**
	 * 校验结果
	 */
	StatusResult result;
	/**
	 * 奖励本身
	 */
	private Rewards<Obj> rewards;
	/**
	 * 操作类型
	 */
	private IOperationType operationType;
	/**
	 * 真实奖励
	 */
	private final List<IRealReward> realRewards = Lists.newArrayListWithCapacity(5);

	private RewardContext(){}
	static <Obj extends IThreadSafe & IPlayer> RewardContext<Obj> valueOf(int multi, Obj player, Rewards<Obj> rewards, IOperationType operationType) {
		RewardContext<Obj> context = new  RewardContext<>();
		context.operationType = operationType;
		context.rewards = rewards;
		context.player = player;
		context.multi = multi;
		return context;
	}

	/**
	 * 发放奖励
	 */
	public void grant(){
		rewards.grant(player, this);
	}

	public Obj getPlayer() {
		return player;
	}

	public int getMulti() {
		return multi;
	}

	public boolean isSuccess(){
		return result == null || result.isSuccess();
	}

	public boolean isFail(){
		return !isSuccess();
	}

	public <T extends Enum<T> & IOperationType> T getOperationType() {
		return (T)operationType;
	}

	public StatusResult getResult() {
		return result;
	}

	public List<IRealReward> getRealRewards() {
		return realRewards;
	}
}
