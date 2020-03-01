package org.qiunet.utils.properties;

import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.HashMap;

/**
 * 加载properties 的工具类. 可以加载中文
 */
public abstract class LoaderProperties extends KeyValueData<Object, Object> {
	protected Logger logger = LoggerType.DUODUO.getLogger();
	protected String fileName;
	/***
	 * 要求相对 classpath的地址
	 */
	public LoaderProperties(String fileName){
		super(new HashMap<>());

		this.fileName = fileName;
		this.reload();
	}
	/**
	 * 重新加载
	 */
	public final void reload(){
		super.load(PropertiesUtil.loadProperties(fileName).returnMap());
		this.onReloadOver();
	}

	/**
	 * 加载完成. 如果子类需要做什么. 覆盖这个方法.
	 */
	protected void onReloadOver(){}
}
