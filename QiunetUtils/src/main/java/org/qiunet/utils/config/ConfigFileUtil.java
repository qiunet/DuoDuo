package org.qiunet.utils.config;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.exceptions.CustomException;
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
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/***
 * 文件类型配置工具
 *
 * @author qiunet
 * 2021/10/28 18:12
 **/
public class ConfigFileUtil {
	private static final Logger logger = LoggerType.DUODUO.getLogger();

	/***
	 * 加载一个properties
	 * @param propertiesFile classpath 目录下的文件
	 * @return
	 */
	public static Properties loaderProperties(File propertiesFile) {
		LoggerType.DUODUO.info("Load config file from {}", propertiesFile.getAbsolutePath());
		Preconditions.checkNotNull(propertiesFile);
		Preconditions.checkArgument(propertiesFile.getName().endsWith("properties"), "file must be a properties file");

		Properties tempProperties = new Properties();
		try (InputStream fis = Files.newInputStream(propertiesFile.toPath());
			 InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)){
			tempProperties.load(isr);
		} catch (Exception e) {
			logger.error("[LoaderProperties] Exception: ", e);
		}
		return tempProperties;
	}

	/**
	 * 加载 Hocon 类型文件
	 * @param fileName classpath 目录下的文件
	 * @return
	 */
	public static Config loadConf(String fileName) {
		URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
		assert resource != null;
		LoggerType.DUODUO.info("Load config from {}", resource.getFile());
		Config config = ConfigFactory.load(fileName);
		config.resolve();
		return config;
	}

	/***
	 * 加载一个 config file
	 * @param fileName classpath 目录下的文件
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadConfig(String fileName) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);
		File file = new File(url.getFile());

		if (fileName.endsWith(".properties")) {
			return new KeyValueData<>(loaderProperties(file));
		}else if (fileName.endsWith(".conf")) {
			Config config = loadConf(fileName);
			Map<Object, Object> collect = config.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, en -> en.getValue().unwrapped().toString()));
			return new KeyValueData<>(collect);
		}
		throw new CustomException("Not support!");
	}

	/***
	 * 加载一个配置文件
	 * @param fileName classpath 目录下的相对地址
	 * @param changeCallback 文件如果变动的回调.
	 * @return
	 */
	public static IKeyValueData<Object, Object> loadConfig(String fileName, IFileChangeCallback changeCallback) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);

		File file = new File(url.getFile());
		FileUtil.changeListener(file, changeCallback);
		return loadConfig(fileName);
	}
}
