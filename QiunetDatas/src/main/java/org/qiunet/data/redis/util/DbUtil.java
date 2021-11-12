package org.qiunet.data.redis.util;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;

public final class DbUtil {
	/**支持分表数量**/
	private static final int MAX_TABLE_FOR_TB_SPLIT = 10;

	private static final int [] POW10_NUMS = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
	/***
	 * hashcode 如果 == Integer.MIN_VALUE 时候, Math.abs 还是等于自己
	 * @param key
	 * @return
	 */
	private static int hashCode(Object key) {
		int hashCode = key.hashCode();
		if (hashCode == Integer.MIN_VALUE) {
			hashCode += 1;
		}
		return Math.abs(hashCode);
	}


	/**
	 * 使用服务器自己的ServerId获得分表索引
	 * @param key openId playerId 等主键ID
	 * @return
	 */
	public static int getTbIndex(Object key) {
		int groupId = ServerConfig.getServerGroupId();
		return getTbIndex(key, groupId);
	}

	/**
	 * 获得分表索引
	 * @param key id openId playerId 等主键ID
	 * @param groupId 服务组id
	 * @return
	 */
	public static int getTbIndex(Object key, int groupId) {
		int length = ServerType.getGroupIdLength(groupId);
		int pow = POW10_NUMS[length + 1];
		int code = hashCode(key);
		if (code <= pow) {
			// 可能key 不是按照规则生成的. 直接取最后的数字即可. 否则都是0
			return code % MAX_TABLE_FOR_TB_SPLIT;
		}
		return (code / pow) % MAX_TABLE_FOR_TB_SPLIT;
	}

	 /***
	 * 使用服务器自己的serverId合成一个唯一的id.
	 * 适用于: 公会id  玩家id
	 * @param incrId 自增id
	 * @return
	 */
	public static long buildId(long incrId) {
		return buildId(incrId, ServerConfig.getServerGroupId());
	}
	/***
	 * 合成一个唯一的id.
	 * 适用于: 公会id  玩家id
	 * @param incrId 自增id
	 * @param serverGroupId 服务器组id
	 * @return
	 */
	public static long buildId(long incrId, int serverGroupId) {
		int length = ServerType.getGroupIdLength(serverGroupId);
		long pow = POW10_NUMS[length + 1];
		return incrId * pow + serverGroupId * 10L + length;
	}

	/**
	 * 根据id. 获得serverGroupId
	 * @param id
	 * @return
	 */
	public static int getServerGroupId(long id) {
		int serverGroupIdLength = (int) (id % 10);
		long pow = POW10_NUMS[serverGroupIdLength + 1];
		return (int) (id % pow) / 10;
	}

	/***
	 * 取到doName 对应的nameSpace
	 * @param doName
	 * @return
	 */
	public static String getNameSpace(String doName) {
		return getDefaultTableName(doName);
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

	public static int getMaxTableForTbSplit(){
		return MAX_TABLE_FOR_TB_SPLIT;
	}
}
