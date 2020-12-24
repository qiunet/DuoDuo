package org.qiunet.utils.config.hocon;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * 对 conf 文件后缀内容的封装
 * idea  conf使用HOCON插件
 *
 * @author qiunet
 * 2020-12-24 20:26
 */
public final class DHocon extends KeyValueData<String, String> {
	/**
	 * @param fileName 基于classpath的文件名和路径
	 */
	public DHocon(String fileName) {
		this(fileName, false);
	}
	/**
	 *
	 * @param fileName 基于classpath的文件名和路径
	 * @param watch 文件变动监听.
	 */
	public DHocon(String fileName, boolean watch) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);

		File file = new File(url.getFile());
		if (watch) {
			FileUtil.changeListener(file, this::load0);
		}
		load0(file);
	}

	private void load0(File file) {
		Config config = ConfigFactory.parseFile(file);
		Map<String, String> collect = config.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, en -> en.getValue().render()));
		super.load(collect);
	}
}
