package org.qiunet.project.init.define;

import org.qiunet.data.cache.entity.CacheEntityList;
import org.qiunet.project.init.enums.EntityType;

/***
 *
 *
 * qiunet
 * 2019-08-20 10:26
 ***/
public class CacheEntityListDefine extends BaseEntityListDefine {

	public CacheEntityListDefine() {
		super(EntityType.CACHE_ENTITY_LIST, CacheEntityList.class);
	}

	@Override
	protected String realTableName() {
		StringBuilder sb = new StringBuilder(getTableName());
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
}
