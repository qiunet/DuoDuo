package org.qiunet.data.redis.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.redis.constants.RedisDbConstants;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.utils.string.StringUtil;

import java.util.Map;

public final class DbUtil {
	/**
	 * 每个entity 对应的源.
	 */
	private static final Map<Class<? extends IEntity>, String> dbSources = Maps.newConcurrentMap();

	public static int getDbIndex(Object key) {
		return (Math.abs(key.hashCode()) % RedisDbConstants.MAX_DB_COUNT);
	}

	public static String getDbName(Object key) {
		return RedisDbConstants.DB_NAME_PREFIX + getDbIndex(key);
	}

	public static String getDbSourceKey(Object key) {
		return ServerConfig.instance.getMoreDbSourcePre() + String.valueOf(getDbIndex(key)
				/ RedisDbConstants.DB_SIZE_PER_INSTANCE);
//		return "split_db_" + String.valueOf(getDbIndex(key)
//				/ RedisDbConstants.DB_SIZE_PER_INSTANCE);
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
		return getDefaultTableName(doName);
	}

	/***
	 * 得到表名 保留驼峰.
	 * @param doName
	 * @return
	 */
	public static String getDefaultTableName(String doName) {
		if (!doName.endsWith("Do")) {
			throw new IllegalArgumentException("Do must end with Do");
		}
		int index = 0;
		StringBuilder sb = new StringBuilder();
		while (index < doName.length() - 2) {
			char c = doName.charAt(index);
			if (c >= 'A' && c <= 'Z') {
				if (index > 0) sb.append('_');
				sb.append((char) (c + 32));
			} else {
				sb.append(c);
			}
			index++;
		}
		return sb.toString();
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
			Preconditions.checkNotNull(table, "Class [" + key.getName() + "] not set `Table` annotation !");
			String dbSource = table.dbSource();
			if (StringUtil.isEmpty(dbSource) && ServerConfig.isLogicServerType()) {
				dbSource = ServerConfig.getDefaultSource();
			}
			return dbSource;
		});
	}
}
