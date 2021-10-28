package org.qiunet.data.redis.util;

import com.google.common.collect.Maps;
import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.util.ServerConfig;

import java.util.Map;

public final class DbUtil {
	/**支持分表数量**/
	private static final int MAX_TABLE_FOR_TB_SPLIT = 10;

	/**每个entity 对应的源.*/
	private static final Map<Class<? extends IEntity>, String> dbSources = Maps.newConcurrentMap();

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
	 * 获得serverId 长度
	 * @param serverId
	 * @return
	 */
	private static int getServerIdLength(int serverId) {
		return (int) (Math.log10(serverId) + 1);
	}

	/**
	 * 使用服务器自己的ServerId获得分表索引
	 * @param key openId playerId 等主键ID
	 * @return
	 */
	public static int getTbIndex(Object key) {
		int serverId = ServerConfig.getServerId();
		return getTbIndex(key, serverId);
	}

	/**
	 * 获得分表索引
	 * @param key id openId playerId 等主键ID
	 * @param serverId serverId 服务器id
	 * @return
	 */
	public static int getTbIndex(Object key, int serverId) {

		int length = getServerIdLength(serverId);
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
	public static long buildId(int incrId) {
		return buildId(incrId, ServerConfig.getServerId());
	}
	/***
	 * 合成一个唯一的id.
	 * 适用于: 公会id  玩家id
	 * @param incrId 自增id
	 * @param serverId 服务器id
	 * @return
	 */
	public static long buildId(int incrId, int serverId) {
		int length = getServerIdLength(serverId);
		long pow = POW10_NUMS[length + 1];
		return incrId * pow + serverId * 10L + length;
	}

	/**
	 * 根据id. 获得serverId
	 * @param id
	 * @return
	 */
	public static int getServerId(long id) {
		int serverIdLength = (int) (id % 10);
		long pow = POW10_NUMS[serverIdLength + 1];
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
