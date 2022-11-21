package org.qiunet.function.reward;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.function.base.IOperationType;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/***
 * 奖励上下文
 *
 * @author qiunet
 * 2020-12-28 20:45
 */
public class RewardContext<Obj extends IThreadSafe & IPlayer> implements IArgsContainer {
	private final ArgsContainer argsContainer = new ArgsContainer();
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
	private List<IRealReward> realRewards;

	private RewardContext(){}
	static <Obj extends IThreadSafe & IPlayer> RewardContext<Obj> valueOf(Obj player, Rewards<Obj> rewards, IOperationType operationType) {
		RewardContext<Obj> context = new  RewardContext<>();
		context.operationType = operationType;
		context.rewards = rewards;
		context.player = player;
		return context;
	}

	public Rewards<Obj> getRewards() {
		return rewards;
	}

	/**
	 * 发放奖励
	 */
	public void grant(){
		if (isFail()) {
			throw new CustomException("Verify reward result is fail!");
		}
		rewards.grant(player, this);
	}

	public void failThrowException() {
		// == null 说明没有校验.
		if (result.isFail()) {
			throw StatusResultException.valueOf(result);
		}
	}

	public Obj getPlayer() {
		return player;
	}

	public boolean isSuccess(){
		return result != null && result.isSuccess();
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

	/**
	 * 返回一个不可修改的list
	 * @return list
	 */
	public List<IRealReward> getRealRewards() {
		if (realRewards == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(realRewards);
	}

	/**
	 * 添加一个realReward list
	 * @param rewards 需要添加
	 */
	public void addRealReward(List<IRealReward> rewards) {
		if (this.realRewards == null) {
			this.realRewards = Lists.newArrayListWithCapacity(8);
		}
		this.realRewards.addAll(rewards);
	}

	/**
	 * 添加一个或者多个 real reward
	 * @param rewards
	 */
	public void addRealReward(IRealReward ... rewards) {
		this.addRealReward(Arrays.asList(rewards));
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return argsContainer.getArgument(key, computeIfAbsent);
	}
}
