package org.qiunet.data.redis.util;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;

import java.util.StringJoiner;

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
	 * id由 自增ID + serverGroupId + serverGroupId长度描述组成
	 * 比如: 自增ID:123,  组ID:456
	 * 则合成ID为: 1234563  (123 456 3)
	 *
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

	/**
	 * 得到insert 的 statement
	 * @param doName
	 * @return
	 */
	public static String getInsertStatement(String doName) {
		return getNameSpace(doName)+".insert"+ doName;
	}
	/**
	 * 得到 update 的 statement
	 * @param doName
	 * @return
	 */
	public static String getUpdateStatement(String doName) {
		return getNameSpace(doName)+".update"+ doName;
	}
	/**
	 * 得到 select 的 statement
	 * @param doName
	 * @return
	 */
	public static String getSelectStatement(String doName) {
		return getNameSpace(doName)+".select"+ doName;
	}
	/**
	 * 得到 select all 的 statement
	 * @param doName
	 * @return
	 */
	public static String getSelectAllStatement(String doName) {
		return getNameSpace(doName)+".selectAll"+ doName;
	}
	/**
	 * 得到 delete 的 statement
	 * @param doName
	 * @return
	 */
	public static String getDeleteStatement(String doName) {
		return getNameSpace(doName)+".delete"+ doName;
	}

	/***
	 * 得到表名 保留驼峰.
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

	public static int getMaxTableForTbSplit(){
		return MAX_TABLE_FOR_TB_SPLIT;
	}

	/**
	 * 构造redis Key
	 * @param doName
	 * @param keys
	 * @return
	 */
	public static String buildRedisKey(String doName, Object... keys){
		StringJoiner sj = new StringJoiner("#");
		sj.add(doName);
		for (Object key : keys) {
			sj.add(String.valueOf(key));
		}
		return sj.toString();
	}
}
