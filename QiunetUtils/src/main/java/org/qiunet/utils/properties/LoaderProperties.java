package org.qiunet.utils.properties;

import com.google.common.base.Preconditions;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.file.FileLoader;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

/**
 * 加载properties 的工具类. 可以加载中文
 */
public abstract class LoaderProperties extends KeyValueData<Object, Object> {
	protected Logger logger = LoggerType.DUODUO.getLogger();
	protected File file;
	/***
	 * 要求相对 classpath的地址
	 */
	public LoaderProperties(String fileName){
		super(new HashMap<>());

		URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		Preconditions.checkNotNull(url, "fileName %s has not find in classpath", fileName);
		this.file = new File(url.getFile());

		this.reload(file);
		FileLoader.listener(file, this::reload);
	}
	/**
	 * 重新加载
	 */
	private void reload(File file){
		super.load(PropertiesUtil.loadProperties(file).returnMap());
		this.onReloadOver();
	}

	/**
	 * 加载完成. 如果子类需要做什么. 覆盖这个方法.
	 */
	protected void onReloadOver(){}
}
