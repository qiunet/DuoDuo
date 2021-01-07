package org.qiunet.utils.config.conf;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.file.IFileChangeCallback;

import java.io.File;
import java.net.URL;

public class HoconUtil {

	public static Config loadConf(File file) {
		return ConfigFactory.parseFile(file);
	}

	public static Config loadConf(String fileName) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);

		return loadConf(new File(url.getFile()));
	}
	/***
	 * 加载一个properties
	 * @param fileName classpath 目录下的相对地址
	 * @param changeCallback 文件如果变动的回调.
	 * @return
	 */
	public static Config loadConf(String fileName, IFileChangeCallback changeCallback) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);

//		File file = new File(url.getFile());
//		FileUtil.changeListener(file, changeCallback);
		return loadConf(fileName);
	}
}
