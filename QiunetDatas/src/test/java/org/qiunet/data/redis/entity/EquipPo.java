package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.RedisList;

/**
 * @author qiunet
 *         Created on 17/1/5 08:41.
 */
@Alias("equipPo")
public class EquipPo extends RedisList {
	public enum FieldEnum implements IField {
		exp,
		level
	}

	private int id;
	private int uid;
	private int level;
	private int exp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
	public String getSubKey() {
		return "id";
	}
	@Override
	public String getDbInfoKeyName() {
		return "uid";
	}
	@Override
	public Integer getSubId() {
		return id;
	}
}
