package org.qiunet.data.cache;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.cache.entity.CacheEntity;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.support.anno.LoadAllData;

@LoadAllData
@Alias("GuildDo")
@Table(name = "guild", dbSource = "basic")
public class GuildDo extends CacheEntity<Long> {
	private long guildId;
	private String name;
	private int level;

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
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

	@Override
	public Long key() {
		return guildId;
	}

	@Override
	public String keyFieldName() {
		return "guildId";
	}
}
