package project.test;

import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.redis.base.IRedisKey;
import org.qiunet.utils.string.StringUtil;

/**
 * Created by qiunet.
 * 17/7/10
 */
public enum  RedisKey implements IRedisKey {
	ITEM,
	LOGIN,
	PLAYER,
	;
	private String key;
	private String asyncKey;

	RedisKey() {
		this.key = name().toLowerCase();
		this.asyncKey = key + "Ac";
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
		return asyncKey + Math.abs(dbInfoKey.hashCode()) % DbProperties.getInstance().getDbIndexList().size();
	}
}
