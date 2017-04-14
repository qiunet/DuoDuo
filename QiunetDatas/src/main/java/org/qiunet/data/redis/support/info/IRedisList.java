package org.qiunet.data.redis.support.info;

import org.qiunet.data.db.support.info.IEntityListDbInfo;

/**
 * 没有平台之分, 但是有多条的 list 数据
 * @author qiunet
 *  Created on 16/12/28 11:53.
 */
public interface IRedisList extends IRedisEntity, IEntityListDbInfo {
	/**
	 * 子主键的组合
	 * 即redis的hash结构中 key field1 value1 file2 value2中的fieldx组合字段
	 * @return  subKey
	 */
	public String getSubKey();
}
