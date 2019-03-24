package org.qiunet.data.redis.key;

import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.redis.base.IRedisKey;
import org.qiunet.utils.string.StringUtil;

/**
 * @author qiunet
 *         Created on 17/2/12 15:52.
 */
public enum  RedisKey implements IRedisKey {
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

	RedisKey(String key, String asyncKey) {
		this.key = key;
		this.asyncKey = asyncKey;
	}
	@Override
	public String getKey(Object dbInfoKey) {
		return this.getKeyByParams(dbInfoKey);
	}

	@Override
	public String getKeyByParams(Object ...moreParams) {
		StringBuilder sb = new StringBuilder();
		sb.append(key).append('#');
		sb.append(StringUtil.arraysToString(moreParams, "", "", "#"));
		return sb.toString();
	}
	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return asyncKey + Math.abs(dbInfoKey.hashCode() % DbProperties.getInstance().getDbIndexList().size());
	}
}
