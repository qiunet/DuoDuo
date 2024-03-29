package org.qiunet.data.db;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.db.entity.DbEntity;

@Alias("PlayerDo")
@Table(name = "player", keyName = "uid", dbSource = "basic")
public class PlayerDo extends DbEntity<Long> {
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
}
