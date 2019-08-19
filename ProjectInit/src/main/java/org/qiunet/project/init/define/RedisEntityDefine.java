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

	public RedisEntityDefine() {
		super(EntityType.REDIS_ENTITY, RedisEntity.class);
	}

	@Override
	protected String realTableName() {
		StringBuilder sb = new StringBuilder("${dbName}.");
		sb.append(getTableName());
		return sb.toString();
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
