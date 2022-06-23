package org.qiunet.function.reward;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;

import java.util.Collections;
import java.util.List;

/***
 * 不可变更的奖励
 *
 * @author qiunet
 * 2020-12-28 20:35
 */
public class UnmodifiableRewards<Obj extends IThreadSafe & IPlayer> extends Rewards<Obj> {
	private List<RewardConfig> rewardConfigs;

	public UnmodifiableRewards(List<RewardConfig> rewardConfigs) {
		super(Collections.emptyList());
		this.rewardConfigs = rewardConfigs;
	}


	@Override
	protected List<BaseReward<Obj>> getRewardList() {
		if (rewardConfigs == null) {
			return super.getRewardList();
		}

		List<BaseReward<Obj>> baseRewardList = Lists.newArrayListWithCapacity(rewardConfigs.size());
		for (RewardConfig rewardConfig : rewardConfigs) {
			BaseReward baseReward = rewardConfig.convertToRewardItem(id -> basicFunction.getResType(id));
			if (baseReward == null) {
				throw new CustomException("rewardConfig {} convert result is null", JsonUtil.toJsonString(rewardConfig));
			}
			baseRewardList.add(baseReward);
		}
		super.baseRewardList = ImmutableList.copyOf(baseRewardList);
		rewardConfigs = null;
		return super.getRewardList();
	}
}
