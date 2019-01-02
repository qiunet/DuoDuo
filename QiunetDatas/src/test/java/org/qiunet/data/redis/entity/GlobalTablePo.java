package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.RedisEntity;

/**
 * @author qiunet
 *         Created on 17/2/27 19:38.
 */
@Alias("GlobalTablePo")
public class GlobalTablePo extends RedisEntity {
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";
	public static final String [] fields = {FIELD_NAME};


	public enum FieldEnum implements IField {
		NAME("name"),
		;
		private String fieldName;
		FieldEnum(String fieldName) {
			this.fieldName = fieldName;
		}
		@Override
		public String getName() {
			return fieldName;
		}
	}

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDbInfoKeyName() {
		return FIELD_ID;
	}
	@Override
	public IField[] getFields() {
		return FieldEnum.values();
	}
}
