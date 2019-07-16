package org.qiunet.data1.db;

import org.apache.ibatis.type.Alias;
import org.qiunet.data1.db.entity.DbEntity;

@Alias("PlayerPo")
public class PlayerPo extends DbEntity<Long, PlayerVo> {
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
	public Long key() {
		return uid;
	}

	@Override
	public String keyFieldName() {
		return "uid";
	}
}
