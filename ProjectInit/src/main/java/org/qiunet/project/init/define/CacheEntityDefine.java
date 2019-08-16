package org.qiunet.project.init.define;


import org.qiunet.data.cache.entity.CacheEntity;
import org.qiunet.project.init.enums.EntityType;

import java.util.Arrays;
import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-08-14 23:14
 ***/
public class CacheEntityDefine extends BaseEntityDefine {

	public CacheEntityDefine() {
		super(EntityType.CACHE_ENTITY, CacheEntity.class);
	}

	@Override
	public List<String> getImportInfos() {
		return Arrays.asList("org.qiunet.data.cache.entity.CacheEntity");
	}
}
