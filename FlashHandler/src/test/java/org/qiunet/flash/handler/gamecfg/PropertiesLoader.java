package org.qiunet.flash.handler.gamecfg;

import org.qiunet.flash.handler.common.annotation.GameProperties;
import org.qiunet.utils.properties.LoaderProperties;

/**
 * Created by qiunet.
 * 17/11/21
 */
@GameProperties(order = 100)
public class PropertiesLoader extends LoaderProperties {
	/***
	 * 要求传入的绝对地址
	 */
	private PropertiesLoader() {
		super(PropertiesLoader.class.getResource("/").getPath() + "test.properties");
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	private volatile static PropertiesLoader instance;

	public static PropertiesLoader getInstance() {
		if (instance == null) {
			synchronized (PropertiesLoader.class) {
				if (instance == null)
				{
					new PropertiesLoader();
				}
			}
		}
		return instance;
	}
}
