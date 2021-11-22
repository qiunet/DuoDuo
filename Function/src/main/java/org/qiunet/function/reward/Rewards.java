package org.qiunet.function.reward;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.base.IOperationType;
import org.qiunet.function.base.IResourceType;
import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.scanner.anno.AutoWired;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/***
 * 奖励
 *
 * @author qiunet
 * 2020-12-28 20:35
 */
public class Rewards<Obj extends IThreadSafe & IPlayer> {
	protected static final TypeReference<List<RewardConfig>> CONFIG_JSON_TYPE = new TypeReference<List<RewardConfig>>(){};
	@AutoWired
	protected static IBasicFunction basicFunction;

	private final List<BaseReward<Obj>> baseRewardList;

	public Rewards() {
		this(Lists.newArrayListWithCapacity(5));
	}

	public Rewards(List<BaseReward<Obj>> baseRewardList) {
		this.baseRewardList = baseRewardList;
	}

	public Rewards(String dbJsonString) {
		this(dbJsonString, false);
	}

	protected Rewards(String dbJsonString, boolean unmodifiable) {
		List<RewardConfig> configList = JsonUtil.getGeneralObjWithField(dbJsonString, CONFIG_JSON_TYPE);
		List<BaseReward<Obj>> baseRewardList = Lists.newArrayListWithCapacity(configList.size());
		for (RewardConfig rewardConfig : configList) {
			baseRewardList.add(rewardConfig.convertToRewardItem(id -> basicFunction.getResType(id)));
		}
		if (unmodifiable) {
			baseRewardList = ImmutableList.copyOf(baseRewardList);
		}

		this.baseRewardList = baseRewardList;
	}
	/**
	 * 校验 是否能扔进背包.
	 * @param player 玩家对象
	 * @param type 操作类型
	 * @return
	 */
	public RewardContext<Obj> verify(Obj player, IOperationType type) {
		return verify(player, 1, type);
	}
	/**
	 * 校验 是否能扔进背包.
	 * @param player 玩家对象
	 * @param multi 倍数
	 * @param type 操作类型
	 * @return
	 */
	public RewardContext<Obj> verify(Obj player, int multi, IOperationType type) {
		if (! player.inSelfThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		RewardContext<Obj> context = RewardContext.valueOf(multi, player, this, type);
		for (BaseReward<Obj> objBaseReward : baseRewardList) {
			StatusResult result = objBaseReward.verify(context);
			if (result.isFail()) {
				context.result = result;
				return context;
			}
		}
		context.verified.compareAndSet(false, true);
		return context;
	}

	/**
	 * 为当前奖励创建一个multi倍数的奖励
	 * @param multi 倍数
	 * @return 新的奖励
	 */
	public Rewards<Obj> createMulti(int multi) {
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
		IResourceType type = basicFunction.getResType(cfgId);
		this.addRewardItem(type.createRewardItem(new RewardConfig(cfgId, count)));
	}

	/**
	 * 添加另一个Rewards
	 * @param rewards
	 */
	public void addRewards(Rewards<Obj> rewards) {
		rewards.baseRewardList.forEach(this::addRewardItem);
	}

	/**
	 * 增加 rewardItem
	 * @param reward 奖励物品
	 */
	public void addRewardItem(BaseReward<Obj> reward) {
		boolean merged = false;
		for (BaseReward<Obj> baseReward : this.baseRewardList) {
			if (baseReward.canMerge(reward)) {
				baseReward.doMerge(reward);
				merged = true;
				break;
			}
		}
		if (! merged) {
			this.baseRewardList.add(reward);
		}
	}
	/**
	 * 循环奖励数据
	 * @param consumer consumer
	 */
	public void forEach(Consumer<BaseReward<Obj>> consumer) {
		baseRewardList.forEach(consumer);
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

		for (BaseReward<Obj> objBaseReward : baseRewardList) {
			objBaseReward.grant(context);
			if (objBaseReward instanceof IRealReward) {
				context.getRealRewards().add((IRealReward) objBaseReward);
			}
		}

		GainRewardEventData.valueOf(this, player, context.getOperationType()).fireEventHandler();
	}

	/**
	 * 转成json
	 * @return
	 */
	public String toDbJsonString(){
		List<RewardConfig> collect = baseRewardList.stream().map(BaseReward::toRewardConfig).collect(Collectors.toList());
		return JsonUtil.toJsonString(collect);
	}
}
