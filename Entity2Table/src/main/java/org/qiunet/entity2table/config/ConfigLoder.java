package org.qiunet.entity2table.config;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.properties.PropertiesUtil;
import org.slf4j.Logger;
//import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于加载 classPath 下的 model2table.properties 文件
 * 加载配置文件
 */
public class ConfigLoder {

	private static final Logger logger = LoggerType.DUODUO.getLogger();
	private static IKeyValueData keyValueData = null;

	private static List<String> modelPack = null;

	static {
		modelPack = new ArrayList<>();
		keyValueData = PropertiesUtil.loadProperties("entity2table.properties");

		logger.error("配置文件 classpath:model2table.properties加载完成。");
	}

	public static String getProperty(String key) {
		return getProperty(key, "");
	}

	public static String getProperty(String key, String defaultValue) {
		return keyValueData.getString(key, defaultValue);
	}

	public static List<String> getModelPack() {
		return modelPack;
	}

	public static void setModelPack(List<String> modelPack) {
		ConfigLoder.modelPack = modelPack;
	}

}
