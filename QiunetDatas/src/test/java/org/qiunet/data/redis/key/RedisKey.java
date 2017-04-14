package org.qiunet.data.redis.key;

import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/2/12 15:52.
 */
public enum  RedisKey {
	FRIEND("friend", "friendAc"),
	
	PLAYER("player", "playerAc"),
	
	EQUIP ("equip", "equipAc"),
	
	SYSMSG("sysmsg", "sysmsgAc"),
	
	LOGIN("login", "loginAc"),
	
	QUNXIU("qunxiu", "qunxiuAc"),

	GLOBAL_TABLE("global_table", "GlobalTableAc"),
	;
	private String key;
	private String asyncKey;
	private RedisKey(String key, String asyncKey) {
		this.key = key;
		this.asyncKey = asyncKey;
	}
	
	public String getKey(Object dbInfoKey) {
		return this.key + "#" + dbInfoKey;
	}
	
	public String getKey(Object dbInfoKey, PlatformType platform){
		return this.key + "#" + dbInfoKey +"#" + platform.getName();
	}
	public static int getAsyncIndex(Object dbInfoKey) {
		return dbInfoKey.hashCode() % DbProperties.getInstance().getDbIndexList().size();
	}
	public String getAsyncKey(Object dbInfoKey) {
		return asyncKey + getAsyncIndex(dbInfoKey);
	}
}
