package org.qiunet.function.rank;

import org.qiunet.data.core.support.redis.IRedisUtil;

import java.util.List;
import java.util.Map;

/***
 * redis 跨服类型的排行.
 *
 * @author qiunet
 * 2020-11-25 15:43
 */
public abstract class BaseRedisRankHandler<Type extends Enum<Type> & IRankType> implements IRankHandler<Type> {
	private IRedisUtil redisUtil;

	public BaseRedisRankHandler(IRedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}


	@Override
	public void updateRank(long id, String name, long value, Map<String, String> extraInfo) {

	}

	@Override
	public RankVo getRankVo(long id) {
		return null;
	}

	@Override
	public List<RankVo> getRankVos(int startRank, int size) {
		return null;
	}

	@Override
	public void clear() {

	}
}
