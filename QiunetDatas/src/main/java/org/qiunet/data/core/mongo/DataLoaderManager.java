package org.qiunet.data.core.mongo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/***
 *
 * @author qiunet
 * 2021/11/17 19:44
 */
enum DataLoaderManager {
	instance;

	private static final Cache<Long, PlayerDataLoader> playerDataLoaders = CacheBuilder.newBuilder()
		.expireAfterAccess(3, TimeUnit.MINUTES)
		.build();

	/**
	 * 取消注册playerId
	 * @param playerId
	 */
	void unRegisterPlayerLoader(long playerId) {
		playerDataLoaders.invalidate(playerId);
	}
	/**
	 * 玩家的数据加载器
	 * 保证如果在一个服务器. 肯定是只存在一个 loader.
	 * @param playerId 玩家id
	 */
	PlayerDataLoader getPlayerLoader(long playerId) {
		try {
			return playerDataLoaders.get(playerId,  () -> new PlayerDataLoader(playerId));
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
