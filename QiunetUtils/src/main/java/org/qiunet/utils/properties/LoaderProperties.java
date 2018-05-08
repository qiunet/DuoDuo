package org.qiunet.utils.properties;

import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * 加载properties 的工具类. 可以加载中文
 */
public abstract class LoaderProperties extends KeyValueData<Object, Object> {
	protected Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	protected String fileName;
	/***
	 * 要求传入的绝对地址
	 */
	public LoaderProperties(String fileName){
		super(new HashMap<>());

		this.fileName = fileName;
		this.reload();
	}
	/**
	 * 加载指定名称的 properties
	 * @return
	 */
	private Properties load(){
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
		return tempProperties;
	}
	/**
	 * 重新加载
	 */
	public final void reload(){
		super.load(load());
		this.onReloadOver();
	}

	/**
	 * 加载完成. 如果子类需要做什么. 覆盖这个方法.
	 */
	protected void onReloadOver(){}
}
