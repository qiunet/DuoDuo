package org.qiunet.data1.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.redis.constants.RedisDbConstants;
import org.qiunet.data1.support.IEntityVo;

public interface IRedisEntityList<Key, SubKey, Vo extends IEntityVo> extends IEntityList<Key, SubKey, Vo>, IRedisEntity<Key, Vo> {
	/**
	 * 如果分表的话.
	 * 得到 tbIndex
	 * (0 - 9)
	 * @return
	 */
	@JSONField(serialize= false, deserialize = false)
	default int getTbIndex(){
		return (Math.abs(key().hashCode()) / RedisDbConstants.MAX_DB_COUNT)
			% RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT;
	}
}
