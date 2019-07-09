package org.qiunet.data1.redis.constants;

public final class RedisDbConstants {
	/**最大支持的库数量**/
	public static final int MAX_DB_COUNT = 100;
	/**分表 数**/
	public static final int MAX_TABLE_FOR_SPLITOR = 10;
	/** 每个数据库实例里面多少数据库 */
	public static final String DB_SIZE_PER_INSTANCE_KEY = "db_size_per_instance";
}
