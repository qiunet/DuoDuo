package org.qiunet.data.util;

import org.qiunet.data.async.SyncType;
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
	public String getDbName(){
		return getString("db_name");
	}

	/***
	 * 同步 或者 异步
	 * 仅缓存和 redis 类型支持
	 * @return
	 */
	public SyncType getSyncType(){
		return getBoolean("async") ? SyncType.ASYNC : SyncType.SYNC;
	}
}
