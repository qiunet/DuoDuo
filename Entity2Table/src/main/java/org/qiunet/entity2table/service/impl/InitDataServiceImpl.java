package org.qiunet.entity2table.service.impl;


import org.qiunet.entity2table.annotation.InitData;
import org.qiunet.entity2table.config.ConfigLoder;
import org.qiunet.entity2table.service.InitDataService;
import org.qiunet.entity2table.utils.ClassTools;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 *
 */
public class InitDataServiceImpl implements InitDataService {

	private static final Logger logger = LoggerType.DUODUO.getLogger();

	private volatile static InitDataServiceImpl instance;

	private InitDataServiceImpl() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static InitDataServiceImpl getInstance() {
		if (instance == null) {
			synchronized (CreateTableServiceImpl.class) {
				if (instance == null)
				{
					new InitDataServiceImpl();
				}
			}
		}
		return instance;
	}

	@Override
	public void initData() {
		logger.info("\n=========开始初始化数据=======");
		Set<Class<?>> classes = ClassTools.getInitDataClasses(ConfigLoder.getModelPack());
		for (Class<?> clas : classes) {
			Method[] methods = clas.getDeclaredMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(InitData.class)) {
					try {
						method.invoke(clas.newInstance());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| InstantiationException e) {
						e.printStackTrace();
					}

				}
			}
		}
		logger.info("\n=========初始化数据结束=======");
	}
}
