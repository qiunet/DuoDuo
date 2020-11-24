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
	private boolean loadAllData;

	public CacheEntityListDefine() {
		super(EntityType.CACHE_ENTITY_LIST, CacheEntityList.class);
	}


	public boolean isLoadAllData() {
		return loadAllData;
	}

	public void setLoadAllData(boolean loadAllData) {
		this.loadAllData = loadAllData;
	}
	@Override
	protected String buildWhereCondition() {
		StringBuilder sb = new StringBuilder(" WHERE ");
		sb.append(key).append(" = #{")
			.append(key).append("} AND ")
			.append(subKey).append(" = #{")
			.append(subKey).append("}");
		return sb.toString();
	}
}
