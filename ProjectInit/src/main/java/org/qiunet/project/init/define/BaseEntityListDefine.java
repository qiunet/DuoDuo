package org.qiunet.project.init.define;

import org.qiunet.data.core.entity.IEntity;
import org.qiunet.project.init.enums.EntityType;

import java.util.Optional;

/***
 *
 *
 * qiunet
 * 2019-08-20 10:08
 ***/
abstract class BaseEntityListDefine extends BaseEntityDefine implements IEntityListDefine {
	/**
	 * subKey的名称
	 */
	protected String subKey;
	/**
	 * 是否分表
	 */
	protected boolean splitTable;

	protected BaseEntityListDefine(EntityType entityType, Class<? extends IEntity> entityClass) {
		super(entityType, entityClass);
	}

	@Override
	public String getSubKeyName() {
		return subKey;
	}

	@Override
	public String getSubKeyType() {
		Optional<FieldDefine> keyField = fieldDefines.stream().filter(f -> f.getName().equals(this.subKey)).findFirst();
		FieldDefine fieldDefine = keyField.orElseThrow(() -> new NullPointerException("DoName ["+this.name+"] have not a field named ["+subKey+"]"));

		switch (fieldDefine.getType()) {
			case "int":
				return "Integer";
			case "String":
				return "String";
			case "long":
				return "Long";
			default:
				throw new IllegalArgumentException("not support key type "+fieldDefine.getType());
		}
	}

	@Override
	public boolean isSplitTable() {
		return splitTable;
	}

	public String getSubKey() {
		return subKey;
	}

	public void setSubKey(String subKey) {
		this.subKey = subKey;
	}

	public void setSplitTable(boolean splitTable) {
		this.splitTable = splitTable;
	}

	@Override
	public String getUpdateSql() {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(realTableName()).append(" SET ");
		for (int i = 0; i < fieldDefines.size(); i++) {
			FieldDefine define = fieldDefines.get(i);
			if (define.getName().equals(key)
			|| define.getName().equals(subKey)) continue;

			sb.append("`").append(define.getName()).append("` = #{")
				.append(define.getName()).append("}");
			if (i < fieldDefines.size() - 1) sb.append(", ");
		}
		sb.append(" ").append(buildWhereCondition()).append(";");
		return sb.toString();
	}
}
