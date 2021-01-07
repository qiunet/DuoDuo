package org.qiunet.utils.config.properties;

import com.google.common.base.Preconditions;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * properties 封装类.
 *
 * @author qiunet
 */
public final class DProperties extends KeyValueData<String, String> {
	/**
	 *
	 * @param fileName 基于classpath的文件名和路径
	 */
	public DProperties(String fileName) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);

		File file = new File(url.getFile());
		load0(file);
	}

	private void load0(File file) {
		Properties properties = PropertiesUtil.loaderProperties(file);
		Map<String, String> collect = properties.entrySet().stream()
				.collect(Collectors.toMap(en -> en.getKey().toString(), en -> en.getValue().toString()));
		super.load(collect);
	}
}
