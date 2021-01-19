package org.qiunet.data.redis.constants;

import org.qiunet.data.util.ServerConfig;

public final class RedisDbConstants {
	/**
	 * 支持的分库数量
	 **/
	public static final int MAX_DB_COUNT = 100;
	/**
	 * 支持分表数量
	 **/
	public static final int MAX_TABLE_FOR_TB_SPLIT = 10;

	/**
	 * 每个数据库实例里面多少数据库
	 */
	public static final int DB_SIZE_PER_INSTANCE = ServerConfig.instance.getDbSizePerInstance();

	/**
	 * redis时候, 因为多个库公用一个源, 需要带入数据库名. 数据名的前缀
	 **/
	public static final String DB_NAME_PREFIX = ServerConfig.instance.getDbNamePrefix();
}
