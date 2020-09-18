package org.qiunet.utils.properties;

import com.google.common.base.Preconditions;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.file.IFileChangeCallback;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class PropertiesUtil {
	private static Logger logger = LoggerType.DUODUO.getLogger();

	/***
	 * 加载一个properties
	 * @param propertiesFile classpath 目录下的文件
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadProperties(File propertiesFile) {
		Preconditions.checkNotNull(propertiesFile);
		Preconditions.checkArgument(propertiesFile.getName().endsWith("properties"), "file must be a properties file");

		Properties tempProperties = new Properties();
		try (InputStream fis = new FileInputStream(propertiesFile);
			 InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)){
			tempProperties.load(isr);
		} catch (Exception e) {
			logger.error("[LoaderProperties] Exception: ", e);
		}
		return new KeyValueData<>(tempProperties);
	}
	/***
	 * 加载一个properties
	 * @param fileName classpath 目录下的相对地址
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadProperties(String fileName) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);

		return loadProperties(new File(url.getFile()));
	}
	/***
	 * 加载一个properties
	 * @param fileName classpath 目录下的相对地址
	 * @param changeCallback 文件如果变动的回调.
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadProperties(String fileName, IFileChangeCallback changeCallback) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);

		File file = new File(url.getFile());
		FileUtil.changeListener(file, changeCallback);
		return loadProperties(fileName);
	}
}
