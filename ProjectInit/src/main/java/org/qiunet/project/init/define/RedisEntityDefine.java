package org.qiunet.project.init.define;

import org.qiunet.data.redis.entity.RedisEntity;
import org.qiunet.project.init.enums.EntityType;


/***
 *
 *
 * qiunet
 * 2019-08-14 23:14
 ***/
public class RedisEntityDefine extends BaseEntityDefine {

	private String redis;

	private boolean loadAllData;

	public RedisEntityDefine() {
		super(EntityType.REDIS_ENTITY, RedisEntity.class);
	}

	public boolean isLoadAllData() {
		return loadAllData;
	}

	public void setLoadAllData(boolean loadAllData) {
		this.loadAllData = loadAllData;
	}
	@Override
	protected String buildWhereCondition() {
		return "WHERE " + getKey() + " = #{" + getKey()+ "}";
	}

	public String getRedis() {
		return redis;
	}

	public void setRedis(String redis) {
		this.redis = redis;
	}
}
