package org.qiunet.function.item_change.reward;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.ItemChangeConfig;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.List;

/***
 * 不可变更的奖励
 *
 * @author qiunet
 * 2020-12-28 20:35
 */
public class UnmodifiableRewards<Obj extends IThreadSafe & IPlayer> extends Rewards<Obj> {
	private List<ItemChangeConfig> itemChangeConfigs;

	public UnmodifiableRewards(List<ItemChangeConfig> itemChangeConfigs) {
		this.itemChangeConfigs = itemChangeConfigs;
	}

	public void loadData() {
		if (itemChangeConfigs == null) {
			return;
		}

		for (ItemChangeConfig itemChangeConfig : itemChangeConfigs) {
			BaseCfgReward<Obj> baseReward = itemChangeConfig.convertToRewardItem();
			if (baseReward == null) {
				throw new CustomException("rewardConfig {} convert result is null", JsonUtil.toJsonString(itemChangeConfig));
			}
			elements.add(baseReward);
		}
		this.itemChangeConfigs = null;
		this.setUnmodifiable();
	}
}
