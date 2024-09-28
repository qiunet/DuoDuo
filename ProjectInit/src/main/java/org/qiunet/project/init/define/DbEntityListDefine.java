package org.qiunet.project.init.define;

import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.project.init.enums.EntityType;

/***
 *
 *
 * qiunet
 * 2019-08-20 10:26
 ***/
public class DbEntityListDefine extends BaseEntityListDefine {

	public DbEntityListDefine() {
		super(EntityType.DB_ENTITY_LIST, DbEntityList.class);
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
