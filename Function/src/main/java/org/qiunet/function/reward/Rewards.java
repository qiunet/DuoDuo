package org.qiunet.function.reward;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.base.IOperationType;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;

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
	private final List<RewardItem<Obj>> rewardItemList;

	public Rewards() {
		this(Lists.newArrayListWithCapacity(5));
	}

	public Rewards(List<RewardItem<Obj>> rewardItemList) {
		this.rewardItemList = rewardItemList;
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
		for (RewardItem<Obj> objRewardItem : rewardItemList) {
			RewardResult result = objRewardItem.verify(player, type);
			if (result.isFail()) {
				context.result = result;
				return context;
			}
		}
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
	 * 增加 rewardItem
	 * @param rewardItem 奖励物品
	 */
	public void addRewardItem(RewardItem<Obj> rewardItem) {
		this.rewardItemList.add(rewardItem);
	}
	/**
	 * 循环奖励数据
	 * @param consumer consumer
	 */
	public void forEach(Consumer<RewardItem<Obj>> consumer) {
		rewardItemList.forEach(consumer);
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

		for (RewardItem<Obj> objRewardItem : rewardItemList) {
			objRewardItem.grant(context);
		}
	}

	/**
	 * 转成json
	 * @return
	 */
	public String toDbString(){
		List<RewardConfig> collect = rewardItemList.stream().map(RewardItem::toRewardConfig).collect(Collectors.toList());
		return JsonUtil.toJsonString(collect);
	}
}
