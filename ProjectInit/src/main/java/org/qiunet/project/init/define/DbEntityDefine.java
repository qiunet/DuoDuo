package org.qiunet.project.init.define;

import org.qiunet.data.db.entity.DbEntity;
import org.qiunet.project.init.enums.EntityType;

/***
 *
 *
 * qiunet
 * 2019-08-14 23:14
 ***/
public class DbEntityDefine extends BaseEntityDefine {

	@Override
	protected String realTableName() {
		return getTableName();
	}

	@Override
	protected String buildWhereCondition() {
		return "WHERE `" + getKey() + "` = #{" + getKey()+ "}";
	}

	public DbEntityDefine() {
		super(EntityType.DB_ENTITY, DbEntity.class);
	}
}
