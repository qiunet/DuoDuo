package org.qiunet.quartz;

import org.qiunet.utils.properties.LoaderProperties;

/**
 * Created by zhengj
 * Date: 2020/8/31.
 * Time: 20:40.
 * To change this template use File | Settings | File Templates.
 */
public class CronConfigProperties extends LoaderProperties {
	private volatile static CronConfigProperties instance;

	public CronConfigProperties() {
		super("cronconfig.properties");
		instance = this;
	}

	public static CronConfigProperties getInstance() {
		if (instance == null) {
			synchronized (CronConfigProperties.class) {
				if (instance == null) {
					new CronConfigProperties();
				}
			}
		}
		return instance;
	}

	public String getProperty(String key) {
		return getString(key);
	}
}
