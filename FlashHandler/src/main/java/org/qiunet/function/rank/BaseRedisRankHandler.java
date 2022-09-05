package org.qiunet.function.rank;

import com.google.common.base.Preconditions;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.json.JsonUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/***
 * redis 跨服类型的排行.
 *
 * @author qiunet
 * 2020-11-25 15:43
 */
public abstract class BaseRedisRankHandler<Type extends Enum<Type> & IRankType> implements IRankHandler<Type> {
	/**
	 * 存放排名
	 */
	private final String redisKey;
	/**
	 * 存放排名相应的数据
	 */
	private final String redisDataKey;

	public BaseRedisRankHandler() {
		this.redisDataKey = "_GAME_RANK_DATA_"+getType().name();
		this.redisKey = "_GAME_RANK_"+getType().name();
	}

	protected  abstract IRedisUtil redisUtil();
	/**
	 * 按照时间构造一个分数.
	 * 分数一样. 按照时间排序
	 *
	 * @param value
	 * @return
	 */
	private long buildScore(long value) {
		Preconditions.checkState(value > 0);
		long val =  value * 100_000_000L;
		val += DateUtil.currSeconds();
		if (val < 0) val = Long.MAX_VALUE;
		return val;
	}

	@Override
	public void updateRank(RankData rankData) {
		String string = JsonUtil.toJsonString(rankData);
		long count = redisUtil().execCommands(jedis -> {
			String strId = String.valueOf(rankData.getId());
			jedis.zadd(redisKey, this.buildScore(rankData.getValue()), strId);
			jedis.hset(redisDataKey, strId, string);

			Long card = redisUtil().returnJedis().zcard(redisKey);
			return card == null ? 0 : card;
		});
		if (count <= getType().rankSize()) {
			return;
		}

		this.trimRank();
	}

	/**
	 * 限制长度
	 */
	private void trimRank(){
		redisUtil().execCommands(jedis -> {
			Set<String> strings = jedis.zrevrange(redisKey, getType().rankSize(), Integer.MAX_VALUE);
			if (strings == null || strings.isEmpty()) {
				return null;
			}
			String[] array = strings.toArray(new String[0]);
			jedis.hdel(redisDataKey, array);
			jedis.zrem(redisKey, array);
			return null;
		});
	}

	@Override
	public RankData getRankVo(long id) {
		return redisUtil().execCommands(jedis -> {
			Long rank = jedis.zrevrank(redisKey, String.valueOf(id));
			if (rank == null) {
				return null;
			}
			 String data = jedis.hget(redisDataKey, String.valueOf(id));
			 RankData rankData = JsonUtil.getGeneralObj(data, RankData.class);
			 rankData.rank = rank.intValue() + 1;
			 return rankData;
		 });
	}

	@Override
	public List<RankData> getRankVos(int startRank, int size) {
		// redis sort set 的排名是从0开始
		startRank--;

		// 本质是list
		Set<String> range = redisUtil().returnJedis().zrevrange(redisKey, startRank, startRank + size);
		List<String> strings = redisUtil().returnJedis().hmget(redisDataKey, range.toArray(new String[0]));
		AtomicInteger rank = new AtomicInteger(startRank);
		return strings.stream().map(str -> {
			RankData rankData = JsonUtil.getGeneralObj(str, RankData.class);
			rankData.rank = rank.incrementAndGet();
			return rankData;
		}).collect(Collectors.toList());
	}

	@Override
	public void clear() {
		redisUtil().execCommands(jedis -> {
			jedis.del(redisDataKey);
			jedis.del(redisKey);
			return null;
		});
	}
}
