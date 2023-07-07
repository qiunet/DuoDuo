package org.qiunet.data.db.loader;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.qiunet.data.async.IAsyncNode;
import org.qiunet.data.support.DbDataListSupport;
import org.qiunet.data.support.DbDataSupport;
import org.qiunet.data.support.IDataSupport;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerStartupEvent;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
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
	/**
	 *
	 * @param clazz
	 * @param playerId
	 * @return
	 */
	Object getData(Class<? extends DbEntityBo> clazz, long playerId) {
		IDataSupport dataSupport = dataSupportMap.get(clazz);
		if (dataSupport instanceof DbDataListSupport) {
			return ((DbDataListSupport) dataSupport).getBoMap(playerId);
		}
		return ((DbDataSupport) dataSupport).getBo(playerId);
	}

	private static final Map<Class<? extends DbEntityBo>, IDataSupport> dataSupportMap = Maps.newHashMapWithExpectedSize(128);

	@Override
	public void syncToDatabase() {
		for (PlayerDataLoader loader : playerDataLoaders.asMap().values()) {
			loader.syncToDb();
		}
	}

	private enum DataLoaderManager0 implements IApplicationContextAware {
		instance;

		@Override
		public ScannerType scannerType() {
			return ScannerType.PLAYER_DATA_LOADER;
		}

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Field> fields = context.getFieldsAnnotatedWith(DataLoader.class);
			for (Field field : fields) {
				DataLoader annotation = field.getAnnotation(DataLoader.class);
				Object instance = context.getInstanceOfClass(field.getDeclaringClass());
				ReflectUtil.makeAccessible(field);

				IDataSupport dataSupport = (IDataSupport)field.get(instance);
				if (dataSupportMap.containsKey(annotation.value())) {
					throw new CustomException("Class {} Register DataLoader Duplicate!", annotation.value().getName());
				}
				dataSupportMap.put(annotation.value(), dataSupport);
			}
		}
	}
}
