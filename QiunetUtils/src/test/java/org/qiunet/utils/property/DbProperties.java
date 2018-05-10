package org.qiunet.utils.property;

import org.qiunet.utils.properties.LoaderProperties;

/**
 * @author qiunet
 *         Created on 17/1/5 16:55.
 */
public class DbProperties extends LoaderProperties {
	/***
	 * 要求传入的绝对地址
	 */
	private DbProperties() {
		super("db.properties");
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
