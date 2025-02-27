package org.qiunet.data.core.mongo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.qiunet.data.async.IAsyncNode;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerStartupEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/***
 *
 * @author qiunet
 * 2021/11/17 19:44
 */
enum DataLoaderManager implements IAsyncNode {
	instance;

	private static final Cache<Long, PlayerDataLoader> playerDataLoaders = CacheBuilder.newBuilder()
		.expireAfterAccess(3, TimeUnit.MINUTES)
		.removalListener(n -> {
			// 被到时移除. 被主动移除. 都remove
			if (n.getValue() != null) {
				((PlayerDataLoader) n.getValue()).remove();
			}
		})
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
	 * @param supplier 生成器
	 */
	public PlayerDataLoader getPlayerLoader(long playerId, Supplier<PlayerDataLoader> supplier) {
		try {
			return playerDataLoaders.get(playerId, supplier::get);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@EventListener
	private void serverStartup(ServerStartupEvent event) {
		this.addToAsyncJob();
	}

	@Override
	public void syncToDatabase() {
		for (PlayerDataLoader loader : playerDataLoaders.asMap().values()) {
			loader.syncToDb();
		}
	}
}
