package org.qiunet.function.reward;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.cfg.base.ICfgDelayLoadData;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.base.IOperationType;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.List;
import java.util.function.Consumer;

/***
 * 奖励
 *
 * @author qiunet
 * 2020-12-28 20:35
 */
public class Rewards<Obj extends IThreadSafe & IPlayer> implements ICfgDelayLoadData {

	protected List<BaseReward<Obj>> baseRewardList;

	public Rewards() {
		this(Lists.newArrayListWithCapacity(5));
	}

	public Rewards(List<BaseReward<Obj>> baseRewardList) {
		this.baseRewardList = baseRewardList;
	}

	/**
	 * 校验 是否能扔进背包.
	 * @param player 玩家对象
	 * @param type 操作类型
	 * @return
	 */
	public RewardContext<Obj> verify(Obj player, IOperationType type) {
		if (! player.inSelfThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		RewardContext<Obj> context = RewardContext.valueOf(player, this, type);
		for (BaseReward<Obj> objBaseReward : this.getRewardList()) {
			StatusResult result = objBaseReward.verify(context);
			if (result.isFail()) {
				context.result = result;
				return context;
			}
		}
		context.result = StatusResult.SUCCESS;
		return context;
	}

	/**
	 * 为当前奖励创建一个multi倍数的奖励
	 * @param multi 倍数
	 * @return 新的奖励
	 */
	public Rewards<Obj> createMulti(int multi) {
		Preconditions.checkState(multi > 0);

		Rewards<Obj> rewards = new Rewards<>();
		this.forEach(item -> rewards.addRewardItem(item.copy(multi)));
		return rewards;
	}

	/**
	 * 添加奖励
	 * @param cfgId 资源id
	 * @param count 数量
	 */
	public void addRewardItem(int cfgId, long count) {
		RewardConfig rewardConfig = new RewardConfig(cfgId, count);
		this.addRewardItem(rewardConfig.convertToRewardItem());
	}

	/**
	 * 添加另一个Rewards
	 * @param rewards
	 */
	public void addRewards(Rewards<Obj> rewards) {
		rewards.getRewardList().forEach(this::addRewardItem);
	}

	/**
	 * 增加 rewardItem
	 * @param reward 奖励物品
	 */
	public void addRewardItem(BaseReward<Obj> reward) {
		boolean merged = false;
		for (BaseReward<Obj> baseReward : this.getRewardList()) {
			if (baseReward.canMerge(reward)) {
				baseReward.doMerge(reward);
				merged = true;
				break;
			}
		}
		if (! merged) {
			this.getRewardList().add(reward);
		}
	}
	/**
	 * 循环奖励数据
	 * @param consumer consumer
	 */
	public void forEach(Consumer<BaseReward<Obj>> consumer) {
		getRewardList().forEach(consumer);
	}

	/**
	 * 发放奖励
	 * @param player 玩家主体
	 * @param context 上下文
	 */
	void grant(Obj player, RewardContext<Obj> context) {
		if (! player.inSelfThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		for (BaseReward<Obj> objBaseReward : getRewardList()) {
			objBaseReward.grant(context);
			if (IRealReward.class.isAssignableFrom(objBaseReward.getClass())) {
				context.addRealReward((IRealReward) objBaseReward);
			}
		}

		GainRewardEvent.valueOf(context).fireEventHandler();
	}

	protected List<BaseReward<Obj>> getRewardList() {
		return baseRewardList;
	}

	/**
	 * 转成json
	 * @return
	 */
	public String toDbJsonString(){
		List<RewardConfig> collect = getRewardList().stream().map(BaseReward::toRewardConfig).toList();
		return JsonUtil.toJsonString(collect);
	}

	@Override
	public void loadData() {
		// do nothing
	}
}
