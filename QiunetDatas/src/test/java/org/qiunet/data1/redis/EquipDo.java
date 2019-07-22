package org.qiunet.data1.redis;

import org.qiunet.data1.redis.entity.RedisEntityList;

public class EquipDo extends RedisEntityList<Long, Integer, EquipBo> {
	private long uid;
	private int equip_id;
	private int level;
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
