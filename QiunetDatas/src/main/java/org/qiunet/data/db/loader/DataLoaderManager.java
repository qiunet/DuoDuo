package org.qiunet.data.db.loader;

import com.google.common.collect.Maps;
import org.qiunet.data.async.IAsyncNode;
import org.qiunet.data.db.loader.event.PlayerKickOutEvent;
import org.qiunet.data.support.DbDataListSupport;
import org.qiunet.data.support.DbDataSupport;
import org.qiunet.data.support.IDataSupport;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/***
 *
 * @author qiunet
 * 2021/11/17 19:44
 */
enum DataLoaderManager implements IAsyncNode {
	instance;

	DataLoaderManager(){
		this.addToAsyncJob();
	}

	private static final Map<Long, PlayerDataLoader> playerDataLoaders = Maps.newConcurrentMap();

	void unRegisterPlayerLoader(long playerId) {
		playerDataLoaders.remove(playerId);
	}

	void registerPlayerLoader(long playerId, PlayerDataLoader loader) {
		playerDataLoaders.put(playerId, loader);
	}

	@EventListener
	private void kickOutPlayer(PlayerKickOutEvent event) {
		PlayerDataLoader dataLoader = playerDataLoaders.get(event.getPlayerId());
		dataLoader.destroy();
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
		for (PlayerDataLoader loader : playerDataLoaders.values()) {
			loader.syncToDb();
		}
	}

	private enum DataLoaderManager0 implements IApplicationContextAware {
		instance;

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
