package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

import java.util.HashMap;
import java.util.Map;

public final class DataSupportMapping {

	private static final Map<Class<? extends IEntity> , IDataSupport> dataSupportMap = new HashMap<>(256);

	public static synchronized void addMapping(Class<? extends IEntity> clazz, IDataSupport dataSupport) {
		if (dataSupportMap.containsKey(clazz)) {
			throw new IllegalArgumentException("DataSupport instance for class ["+clazz.getName()+"] is duplicate");
		}
		dataSupportMap.put(clazz, dataSupport);
	}

	public static IDataSupport getMapping(Class<? extends IEntity> clazz) {
		IDataSupport support = dataSupportMap.get(clazz);
		if (support == null) {
			throw new NullPointerException("DataSupport for class ["+clazz.getName()+"] must instance first!");
		}
		return support;
	}
}
