package org.qiunet.data.util;

import java.util.StringJoiner;

public final class DbUtil {
	/***
	 * hashcode 如果 == Integer.MIN_VALUE 时候, Math.abs 还是等于自己
	 * @param key
	 * @return
	 */
	private static long hashCode(Object key) {
		long hashCode;
		if (key instanceof Number) {
			hashCode = ((Number) key).longValue();
		}else {
			hashCode = key.hashCode();
		}
		return Math.abs(hashCode);
	}
	/***
	 * 得到表名 驼峰转下划线
	 * @param doName
	 * @return
	 */
	public static String getDefaultTableName(String doName) {
		if (! doName.endsWith("Do")) {
			throw new IllegalArgumentException("doName ["+doName+"] must end with Do");
		}
		int index = 0;
		StringBuilder sb = new StringBuilder();
		while (index < doName.length() - 2) {
			char c = doName.charAt(index);
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

	/**
	 * 构造redis Key
	 * @param doName
	 * @param keys
	 * @return
	 */
	public static String buildRedisKey(String doName, Object... keys){
		StringJoiner sj = new StringJoiner("#");
		// 同一个redis下, 可能需要区分是哪个组. 因为数据库不一样.
		sj.add(doName);
		for (Object key : keys) {
			sj.add(String.valueOf(key));
		}
		return sj.toString();
	}
}
