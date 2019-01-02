package org.qiunet.data.redis.support;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.data.util.DataUtil;

import java.util.Map;

/**
 * 有id的对象, 非个人数据 单个在redis里面存
 *  工会
 * @author qiunet
 *         Created on 16/12/30 08:05.
 */
public abstract class RedisEntity implements IRedisEntity {

	protected IEntityDbInfo entityDbInfo;

	public void setEntityDbInfo(IEntityDbInfo entityDbInfo) {
		this.entityDbInfo = entityDbInfo;
	}
	@Override
	public Map<String, String> getAllFeildsToHash() {
		return DataUtil.getMap(this, getFields());
	}
	@Override
	public int getFieldCount() {
		return getFields().length;
	}
	@Override
	public String getDbName() {
		return entityDbInfo.getDbName();
	}
	@Override
	public int getDbIndex() {
		return entityDbInfo.getDbIndex();
	}
	@Override
	public String getDbSourceKey() {
		return entityDbInfo.getDbSourceKey();
	}
}
