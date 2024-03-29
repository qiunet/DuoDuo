package org.qiunet.data.redis;

import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.redis.entity.RedisEntity;

@Table(name = "vip", keyName = "uid", dbSource = "basic")
public class VipDo extends RedisEntity<Long> {
	private long uid;
	private int level;
	private long exp;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	@Override
	public Long key() {
		return uid;
	}
}
