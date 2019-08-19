package org.qiunet.data.redis.util;

import org.qiunet.data.redis.constants.RedisDbConstants;

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

	/***
	 * 取到doName 对应的nameSpace
	 * @param doName
	 * @return
	 */
	public static String getNameSpace(String doName) {
		if (doName.endsWith("Do")) {
			return doName.substring(0, doName.length() - 2).toLowerCase();
		}
		return doName;
	}
	/***
	 * 得到表名 保留驼峰.
	 * @param doName
	 * @return
	 */
	public static String getDefaultTableName(String doName) {
		if (! doName.endsWith("Do")) {
			throw new IllegalArgumentException("Do must end with Do");
		}
		String str = doName.substring(0, doName.length() - 2);
		int index = 0;
		StringBuilder sb = new StringBuilder();
		while (index < str.length()) {
			char c = str.charAt(index);
			if (c >= 'A' && c <= 'Z'){
				if (index > 0) sb.append('_');
				sb.append((char)(c + 32));
			} else {
				sb.append(c);
			}
			index++;
		}
		return sb.toString();
	}
}
