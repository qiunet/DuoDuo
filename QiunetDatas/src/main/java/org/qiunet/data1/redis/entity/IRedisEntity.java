package org.qiunet.data1.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.data1.redis.constants.RedisDbConstants;
import org.qiunet.data1.support.IEntityBo;
import org.qiunet.data1.core.entity.IEntity;

public interface IRedisEntity<Key, Bo extends IEntityBo> extends IEntity<Key, Bo> {
	/***
	 * 得到dbName
	 * @return dbName 在sql表前面的
	 */
	@JSONField(serialize= false, deserialize = false)
	default String getDbName(){
		return RedisDbConstants.DB_NAME_PREFIX + getDbIndex();
	}
	/**
	 * 得到dbIndex
	 * 默认是 (0 - 99)
	 * 是哪个数据库
	 * @return
	 */
	@JSONField(serialize= false, deserialize = false)
	default int getDbIndex(){
		return (Math.abs(key().hashCode()) % RedisDbConstants.MAX_DB_COUNT);
	}
	/**
	 * 根据dbIndex 得到是用哪个数据库源的key
	 * @return dbSource dbSource路由抉择使用
	 */
	@JSONField(serialize= false, deserialize = false)
	default String getDbSourceKey(){
		return String.valueOf(getDbIndex()
			/ RedisDbConstants.DB_SIZE_PER_INSTANCE);
	}
}
