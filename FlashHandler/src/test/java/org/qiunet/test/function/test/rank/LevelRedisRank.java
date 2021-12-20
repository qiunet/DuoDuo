package org.qiunet.test.function.test.rank;


import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.function.rank.BaseRedisRankHandler;
import org.qiunet.function.rank.IRankHandler;
import org.qiunet.function.rank.IRankHandlerWrapper;
import org.qiunet.test.cross.common.redis.RedisDataUtil;
import org.qiunet.utils.async.LazyLoader;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 11:51
 */
public enum LevelRedisRank implements IRankHandlerWrapper<RankType> {
	instance;
	private final LazyLoader<IRankHandler<RankType>> rankHandler = new LazyLoader<>(RankHandler::new);

	//@EventListener
	//private void LevelChange(LevelUpEventData eventData) {
	//	rankHandler.get().updateRank(RankData.custom(eventData.getId(), eventData.getLevel()).addName("名称1").build());
	//}

	@Override
	public IRankHandler<RankType> rankHandler() {
		return rankHandler.get();
	}

	private static class RankHandler extends BaseRedisRankHandler<RankType> {

		@Override
		protected IRedisUtil redisUtil() {
			return RedisDataUtil.getInstance();
		}

		@Override
		public RankType getType() {
			return RankType.LEVEL;
		}
	}
}
