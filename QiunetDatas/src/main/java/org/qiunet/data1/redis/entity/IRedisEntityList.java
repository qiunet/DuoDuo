package org.qiunet.data1.redis.entity;

import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.redis.info.IRedisListDbInfo;

public interface IRedisEntityList<Key, SubKey> extends IEntityList<Key, SubKey>, IRedisEntity<Key> {
	/**
	 * 得到redisListDbInfo
	 * @return
	 */
	IRedisListDbInfo<Key> redisListDbInfo();
	/**
	 * 如果分表的话.
	 * 得到 tbIndex
	 * (0 - 9)
	 * @return
	 */
	default int getTbIndex(){
		return redisListDbInfo().getTbIndexByKey(getKey());
	}
}
