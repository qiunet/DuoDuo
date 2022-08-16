package org.qiunet.test.function.test.rank;


import org.qiunet.function.rank.BaseCacheRankHandler;
import org.qiunet.function.rank.IRankHandler;
import org.qiunet.function.rank.IRankHandlerWrapper;
import org.qiunet.function.rank.RankData;
import org.qiunet.test.function.test.targets.event.LevelUpEventData;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.listener.event.EventListener;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 11:51
 */
public enum LevelCacheRank implements IRankHandlerWrapper<RankType> {
	instance;
	private final LazyLoader<IRankHandler<RankType>> rankHandler = new LazyLoader<>(RankHandler::new);

	@EventListener
	private void LevelChange(LevelUpEventData eventData) {
		rankHandler.get().updateRank(RankData.custom(eventData.getPlayer().getId(), eventData.getLevel()).addName(eventData.getPlayer().getOpenId()).build());
	}

	@Override
	public IRankHandler<RankType> rankHandler() {
		return rankHandler.get();
	}

	private static class RankHandler extends BaseCacheRankHandler<RankType> {
		@Override
		protected void save(Collection<RankData> rankDataList) {
		}

		@Override
		protected List<RankData> load() {
			return Collections.emptyList();
		}

		@Override
		public RankType getType() {
			return RankType.LEVEL;
		}
	}
}
