package org.qiunet.data1.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.data1.redis.constants.RedisDbConstants;
import org.qiunet.data1.support.IEntityVo;
import org.qiunet.data1.util.DbProperties;
import org.qiunet.data1.core.entity.IEntity;

public interface IRedisEntity<Key, Vo extends IEntityVo> extends IEntity<Key, Vo> {
	/***
	 * 得到dbName
	 * @return dbName 在sql表前面的
	 */
	@JSONField(serialize= false, deserialize = false)
	default String getDbName(){
		return "";
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
			/ DbProperties.getInstance().getInt(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY));
	}
}
