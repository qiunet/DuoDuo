package org.qiunet.data.redis;

import org.qiunet.data.redis.entity.RedisEntityList;

public class EquipDo extends RedisEntityList<Long, Integer, EquipBo> {
	private long uid;
	private int equip_id;
	private int level;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getEquip_id() {
		return equip_id;
	}

	public void setEquip_id(int equip_id) {
		this.equip_id = equip_id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public Integer subKey() {
		return equip_id;
	}

	@Override
	public String subKeyFieldName() {
		return "equip_id";
	}

	@Override
	public Long key() {
		return uid;
	}

	@Override
	public String keyFieldName() {
		return "uid";
	}
}
