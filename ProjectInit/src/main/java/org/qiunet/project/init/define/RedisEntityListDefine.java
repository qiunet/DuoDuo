package org.qiunet.project.init.define;

import org.qiunet.data.redis.entity.RedisEntityList;
import org.qiunet.project.init.enums.EntityType;

/***
 *
 *
 * qiunet
 * 2019-08-20 10:26
 ***/
public class RedisEntityListDefine extends BaseEntityListDefine {
	private String redis;

	public RedisEntityListDefine() {
		super(EntityType.REDIS_ENTITY_LIST, RedisEntityList.class);
	}

	@Override
	protected String realTableName() {
		StringBuilder sb = new StringBuilder();
		sb.append("${dbName}.").append(getTableName());
		if (isSplitTable()) sb.append("_${tbIndex}");
		return sb.toString();
	}

	@Override
	protected String buildWhereCondition() {
		StringBuilder sb = new StringBuilder(" WHERE ");
		sb.append("`").append(key).append("` = #{")
			.append(key).append("} AND `")
			.append(subKey).append("` = #{")
			.append(subKey).append("}");
		return sb.toString();
	}

	public String getRedis() {
		return redis;
	}

	public void setRedis(String redis) {
		this.redis = redis;
	}
}
