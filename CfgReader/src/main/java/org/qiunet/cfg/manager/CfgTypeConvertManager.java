package org.qiunet.cfg.manager;

import org.qiunet.cfg.convert.ICfgTypeConvert;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CfgTypeConvertManager {
	private static CfgTypeConvertManager instance = new CfgTypeConvertManager();
	private Logger logger = LoggerType.DUODUO.getLogger();
	private CfgTypeConvertManager(){
		if (instance != null) {
			throw new IllegalStateException("Already has instance .");
		}
	}

	public static CfgTypeConvertManager getInstance(){
		if (instance == null) {
			synchronized (CfgTypeConvertManager.class){
				new CfgTypeConvertManager();
			}
		}
		return instance;
	}

	private Map<Class, ICfgTypeConvert> convertMap = new HashMap<>();

	/***
	 * 添加一个convert 处理器.
	 * @param clazz
	 */
	public void addConvertClass(Class<? extends ICfgTypeConvert> clazz) {
		Type[] types = clazz.getGenericInterfaces();
		Class<?> handlerClass = null;
		for (Type type : types) {
			if (type instanceof ParameterizedTypeImpl
				&& ((ParameterizedTypeImpl) type).getRawType() == ICfgTypeConvert.class){
				handlerClass = (Class<?>) ((ParameterizedTypeImpl) type).getActualTypeArguments()[0];
			}
		}

		if (handlerClass == null) {
			throw new RuntimeException("["+clazz.getName()+"] 必须实现ICfgTypeConvert接口, 并且有对应的泛型数据.");
		}
		try {
			this.convertMap.put(handlerClass, clazz.newInstance());
		} catch (Exception e) {
			logger.error("初始化Convert {}失败!", clazz.getName());
		}
	}

	/***
	 * 得到对应的转换器.
	 * 简单的基本类型. 在外面已经处理好了.
	 * @param clazz
	 * @return
	 */
	public ICfgTypeConvert returnConvert(Class clazz) {
		return convertMap.get(clazz);
	}
}
