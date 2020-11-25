package org.qiunet.function.test.rank;


import org.qiunet.function.rank.BaseCacheRankHandler;
import org.qiunet.function.rank.RankVo;
import org.qiunet.function.test.targets.event.LevelUpEventData;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.json.JsonUtil;

import java.util.Collections;
import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 11:51
 */
public class LevelCacheRankHandler extends BaseCacheRankHandler<RankType> {
	@Override
	protected void save(List<RankVo> rankVoList) {
		System.out.println("==================UPDATE::" + JsonUtil.toJsonString(rankVoList));
	}

	@Override
	protected List<RankVo> load() {
		return Collections.emptyList();
	}

	@Override
	public RankType getType() {
		return RankType.LEVEL;
	}

	@EventListener
	public void LevelChange(LevelUpEventData eventData) {
		this.updateRank(eventData.getPlayer().getId(), eventData.getPlayer().getName(), eventData.getLevel());
	}
}
