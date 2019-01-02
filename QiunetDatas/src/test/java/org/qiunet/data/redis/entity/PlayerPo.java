package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.PlatformRedisEntity;

/**
 * @author qiunet
 *         Created on 17/1/5 08:41.
 */
@Alias("playerPo")
public class PlayerPo extends PlatformRedisEntity {
	public enum FieldEnum implements IField {
		LEVEL("level"),
		EXP("exp"),
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
	private int uid;
	private int level;
	private int exp;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	@Override
	public IField[] getFields() {
		return FieldEnum.values();
	}

	@Override
	public String getDbInfoKeyName() {
		return "uid";
	}
}
