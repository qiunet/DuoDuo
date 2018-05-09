package org.qiunet.utils.properties;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public final class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	/***
	 * 加载一个properties
	 * @param fileName
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadProperties(String fileName) {
		Properties tempProperties = new Properties();
		InputStreamReader isr = null ;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(fileName));
			isr = new InputStreamReader(fis , "UTF-8");
			tempProperties.load(isr);
		} catch (Exception e) {
			logger.error("[LoaderProperties] Exception: ", e);
		} finally {
			try {
				if (isr != null) isr.close();
				if (fis != null) fis.close();
			}catch (Exception e) {
				logger.error("[LoaderProperties] Close Exception: ", e);
			}
		}
		return new KeyValueData(tempProperties);
	}

	/***
	 * 从 resources 下加载文件
	 * @param fileName
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadPropertiesFromResourcesPath(String fileName) {
		fileName = PropertiesUtil.class.getResource("/").getPath() + fileName;
		return loadProperties(fileName);
	}
}
