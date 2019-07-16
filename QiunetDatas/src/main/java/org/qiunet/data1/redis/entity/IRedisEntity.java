package org.qiunet.data1.redis.entity;

import org.qiunet.data1.redis.constants.RedisDbConstants;
import org.qiunet.data1.redis.info.IRedisDbInfo;
import org.qiunet.data1.support.IEntityVo;
import org.qiunet.data1.util.DbProperties;
import org.qiunet.data1.core.entity.IEntity;

public interface IRedisEntity<Key, Vo extends IEntityVo> extends IEntity<Key, Vo> {
	/***
	 * 得到redis的dbInfo
	 * @return
	 */
	IRedisDbInfo<Key> redisDbInfo();
	/***
	 * 得到dbName
	 * @return dbName 在sql表前面的
	 */
	String getDbName();
	/**
	 * 得到dbIndex
	 * 默认是 (0 - 99)
	 * 是哪个数据库
	 * @return
	 */
	default int getDbIndex(){
		return redisDbInfo().getDbIndexByKey(key());
	}
	/**
	 * 根据dbIndex 得到是用哪个数据库源的key
	 * @return dbSource dbSource路由抉择使用
	 */
	default String getDbSourceKey(){
		return String.valueOf(getDbIndex() / DbProperties.getInstance().getInt(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY));
	}
}
