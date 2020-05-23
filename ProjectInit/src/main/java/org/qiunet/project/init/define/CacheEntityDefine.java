package org.qiunet.project.init.define;


import org.qiunet.data.cache.entity.CacheEntity;
import org.qiunet.project.init.enums.EntityType;

/***
 *
 *
 * qiunet
 * 2019-08-14 23:14
 ***/
public class CacheEntityDefine extends BaseEntityDefine {

	@Override
	protected String buildWhereCondition() {
		return "WHERE " + getKey() + " = #{" + getKey()+ "}";
	}

	public CacheEntityDefine() {
		super(EntityType.CACHE_ENTITY, CacheEntity.class);
	}
}
