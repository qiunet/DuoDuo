package org.qiunet.data.util;

import org.qiunet.utils.properties.LoaderProperties;

public class DbProperties extends LoaderProperties {
	private volatile static DbProperties instance = new DbProperties();
	public static DbProperties getInstance() {
		return instance;
	}
	/***
	 * 要求相对 classpath的地址
	 */
	private DbProperties() {
		super("db.properties");
	}
}
