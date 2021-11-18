package org.qiunet.data.db.loader;

import com.google.common.collect.Maps;
import org.qiunet.data.support.*;
import org.qiunet.quartz.CronSchedule;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
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
enum DataLoaderManager {
	instance;
	private static final Map<Long, PlayerDataLoader> playerDataLoaders = Maps.newConcurrentMap();

	void unRegisterPlayerLoader(long playerId) {
		playerDataLoaders.remove(playerId);
	}

	void registerPlayerLoader(long playerId, PlayerDataLoader loader) {
		playerDataLoaders.put(playerId, loader);
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

	@CronSchedule("15 * * * * ?")
	void asyncCacheToDb() {
		for (PlayerDataLoader loader : playerDataLoaders.values()) {
			loader.syncToDb();
		}
	}

	private static final Map<Class<? extends DbEntityBo>, IDataSupport> dataSupportMap = Maps.newHashMapWithExpectedSize(128);
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
