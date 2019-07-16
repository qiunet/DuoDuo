package org.qiunet.data1.redis.entity;

import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.redis.info.IRedisListDbInfo;
import org.qiunet.data1.support.IEntityVo;

public interface IRedisEntityList<Key, SubKey, Vo extends IEntityVo> extends IEntityList<Key, SubKey, Vo>, IRedisEntity<Key, Vo> {
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
		return redisListDbInfo().getTbIndexByKey(key());
	}
}
