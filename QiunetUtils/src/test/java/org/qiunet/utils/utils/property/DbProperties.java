package org.qiunet.utils.utils.property;

import org.qiunet.utils.properties.LoaderProperties;

/**
 * @author qiunet
 *         Created on 17/1/5 16:55.
 */
public class DbProperties extends LoaderProperties {
	private static final String filePath ;
	static {
		filePath = DbProperties.class.getResource("/").getPath() + "db.properties";
	}
	/***
	 * 要求传入的绝对地址
	 */
	private DbProperties() {
		super(filePath);
		instance = this;
	}
	private volatile static DbProperties instance;
	
	public static DbProperties getInstance() {
		if (instance == null) {
			synchronized (DbProperties.class) {
				if (instance == null)
				{
					instance = new DbProperties();
				}
			}
		}
		return instance;
	}
}
