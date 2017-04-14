package redis.key;

import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/2/12 15:52.
 */
public enum RedisKey {
	FRIEND,
	
	PLAYER,
	
	EQUIP ,
	
	SYSMSG,
	
	LOGIN,

	QUNXIU,

	GLOBALTABLE,
	;
	private String key;
	private String asyncKey;
	private RedisKey() {
		this.key = name().toLowerCase();
		this.asyncKey = key + "Ac";
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
