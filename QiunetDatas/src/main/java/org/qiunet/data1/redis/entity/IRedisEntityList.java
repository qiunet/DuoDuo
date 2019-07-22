package org.qiunet.data1.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.redis.constants.RedisDbConstants;
import org.qiunet.data1.redis.util.DbUtil;
import org.qiunet.data1.support.IEntityBo;

public interface IRedisEntityList<Key, SubKey, Bo extends IEntityBo> extends IEntityList<Key, SubKey, Bo>, IRedisEntity<Key, Bo> {
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
