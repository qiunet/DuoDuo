package org.qiunet.data1.redis.info;

import org.qiunet.data1.redis.constants.RedisDbConstants;

/**
 *  如果有特殊的, 可以自己实现该接口.
 *  例子:
 *  update ${dbName}.equip_${tbIndex} SET
 *         `level` = #{level},
 *         exp=${exp}
 *         where uid = #{uid} and id = #{subId};
 *
 * @author qiunet
 *         Created on 17/1/6 09:40.
 */
public interface IRedisDbInfo<Key> {

	/***
	 * 得到dbIndex
	 *
	 * @param key
	 * @return
	 */
	default int getDbIndexByKey(Key key){
		return (Math.abs(key.hashCode()) % RedisDbConstants.MAX_DB_COUNT);
	}
}
