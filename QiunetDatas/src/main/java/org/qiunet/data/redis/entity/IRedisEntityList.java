package org.qiunet.data.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.data.core.entity.IEntityList;
import org.qiunet.data.redis.util.DbUtil;

public interface IRedisEntityList<Key, SubKey> extends IEntityList<Key, SubKey>, IRedisEntity<Key> {
	/**
	 * 如果分表的话.
	 * 得到 tbIndex
	 * (0 - 9)
	 * @return
	 */
	@JSONField(serialize= false, deserialize = false)
	default int getTbIndex(){
		return DbUtil.getTbIndex(key());
	}
}
