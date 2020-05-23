package org.qiunet.data.cache;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.cache.entity.CacheEntityList;
import org.qiunet.data.core.support.db.Table;

@Alias("GuildMemberDo")
@Table(name = "guild_member")
public class GuildMemberDo extends CacheEntityList<Long, Long, GuildMemberBo> {
	private long guildId;
	private long memberId;
	private int job;

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public int getJob() {
		return job;
	}

	public void setJob(int job) {
		this.job = job;
	}

	@Override
	public Long subKey() {
		return memberId;
	}

	@Override
	public String subKeyFieldName() {
		return "memberId";
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
