package org.qiunet.data.redis;

import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.redis.entity.RedisEntityList;

@Table(name = "equip", keyName = "uid", subKeyName = "equip_id", dbSource = "basic")
public class EquipDo extends RedisEntityList<Long, Integer> {
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
	public Long key() {
		return uid;
	}
}
