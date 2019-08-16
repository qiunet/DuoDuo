package org.qiunet.project.init.define;

import org.qiunet.data.redis.entity.RedisEntity;
import org.qiunet.project.init.enums.EntityType;

import java.util.Arrays;
import java.util.List;

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

	public String getRedis() {
		return redis;
	}

	public void setRedis(String redis) {
		this.redis = redis;
	}

	@Override
	public List<String> getImportInfos() {
		return Arrays.asList("org.qiunet.data.redis.entity.RedisEntity");
	}
}
