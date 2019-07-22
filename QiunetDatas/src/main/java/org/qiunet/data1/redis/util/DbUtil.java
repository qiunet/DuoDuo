package org.qiunet.data1.redis.util;

import org.qiunet.data1.redis.constants.RedisDbConstants;

public final class DbUtil {

	public static int getDbIndex(Object key) {
		return (Math.abs(key.hashCode()) % RedisDbConstants.MAX_DB_COUNT);
	}

	public static String getDbName(Object key) {
		return RedisDbConstants.DB_NAME_PREFIX + getDbIndex(key);
	}

	public static String getDbSourceKey(Object key) {
		return String.valueOf(getDbIndex(key)
			/ RedisDbConstants.DB_SIZE_PER_INSTANCE);
	}

	public static int getTbIndex(Object key) {
		return (Math.abs(key.hashCode()) / RedisDbConstants.MAX_DB_COUNT)
			% RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT;
	}
}
