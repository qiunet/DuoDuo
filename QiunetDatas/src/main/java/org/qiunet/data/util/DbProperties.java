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

	/**
	 * 取到server的类型.
	 * 0 为普通功能服
	 *
	 * @return
	 */
	public int getServerType() {
		return getInt("serverType", 0);
	}

	public boolean isFuncServerType() {
		return getServerType() == 0;
	}
	/**
	 * 得到serverId
	 * @return
	 */
	public int getServerId(){
		return getInt("serverId");
	}

	/**
	 * 得到默认数据源
	 * @return
	 */
	public String getDefaultDbSource(){
		return getString("default_database_source");
	}
}
