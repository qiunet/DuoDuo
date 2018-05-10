package org.qiunet.utils.properties;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public final class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	/***
	 * 加载一个properties
	 * @param fileName classpath 目录下的相对地址
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadProperties(String fileName) {
		Properties tempProperties = new Properties();
		InputStreamReader isr = null ;
		InputStream fis = null;
		try {
			fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
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
}
