package org.qiunet.project.init.define;

import org.qiunet.data.db.entity.DbEntity;
import org.qiunet.project.init.enums.EntityType;

import java.util.Arrays;
import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-08-14 23:14
 ***/
public class DbEntityDefine extends BaseEntityDefine {

	public DbEntityDefine() {
		super(EntityType.DB_ENTITY, DbEntity.class);
	}

	@Override
	public List<String> getImportInfos() {
		return Arrays.asList("org.qiunet.data.db.entity.DbEntity");
	}
}
