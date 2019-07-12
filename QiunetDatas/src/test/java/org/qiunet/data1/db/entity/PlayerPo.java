package org.qiunet.data1.db.entity;

import org.apache.ibatis.type.Alias;

@Alias("PlayerPo")
public class PlayerPo extends DbEntity<Long> {
	private long uid;
	private String name;
	private int level;
	private long exp;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public Long getKey() {
		return uid;
	}

	@Override
	public String getKeyFieldName() {
		return "uid";
	}
}
