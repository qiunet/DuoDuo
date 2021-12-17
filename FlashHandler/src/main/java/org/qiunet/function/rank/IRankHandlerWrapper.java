package org.qiunet.function.rank;

import java.util.List;

/***
 *
 * rankHandler 的一个封装.
 * 方便rank 单例管理
 *
 * @author qiunet
 * 2021/12/17 14:24
 */
public interface IRankHandlerWrapper<Type extends Enum<Type> & IRankType> extends IRankHandler<Type> {
	/**
	 * 提供rankHandler
	 * @return
	 */
	IRankHandler<Type> rankHandler();

	@Override
	default RankData getRankVo(long id) {
		return rankHandler().getRankVo(id);
	}

	@Override
	default List<RankData> getRankVos(int startRank, int size) {
		return rankHandler().getRankVos(startRank, size);
	}

	@Override
	default Type getType() {
		return rankHandler().getType();
	}

	@Override
	default void updateRank(RankData rankData) {
		rankHandler().updateRank(rankData);
	}

	@Override
	default void clear() {
		rankHandler().clear();
	}
}
