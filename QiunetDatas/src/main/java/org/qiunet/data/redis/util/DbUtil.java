package org.qiunet.data.redis.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.utils.string.StringUtil;

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

	public static int getTbIndex(Object key) {
		int serverId = ServerConfig.getServerId();
		int length = (int) (Math.log10(serverId));
		int pow = POW10_NUMS[length + 1];

		return (hashCode(key) / pow)
			% MAX_TABLE_FOR_TB_SPLIT;
	}

	/**
	 * 得到entity 适用的数据库源名
	 *
	 * @param clazz
	 * @return
	 */
	public static String getDbSource(Class<? extends IEntity> clazz) {
		return dbSources.computeIfAbsent(clazz, key -> {
			Table table = key.getAnnotation(Table.class);
			Preconditions.checkNotNull(table,"Class ["+key.getName()+"] not set `Table` annotation !");
			String dbSource = table.dbSource();
			if (StringUtil.isEmpty(dbSource) && ServerConfig.isLogicServerType()) {
				dbSource = ServerConfig.getDefaultSource();
			}
			return dbSource;
		});
	}

	 /***
	 * 合成一个唯一的id.
	 * 适用于: 公会id  玩家id
	 * @param incrId
	 * @return
	 */
	public static long buildId(int incrId) {
		int serverId = ServerConfig.getServerId();
		int length = (int) (Math.log10(serverId));
		int pow = POW10_NUMS[length + 1];
		return incrId * pow + serverId * 10 + length;
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
