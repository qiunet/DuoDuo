package org.qiunet.entity2table;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;

/**
* *
* 对象为自动创建 不要修改
*/
@Alias("GuildMemberDo")
@Table(name = "guild_member", keyName = "guildId", subKeyName = "memberId", dbSource = "global")
public class GuildMemberDo {
	@Column(comment = "公会id", isKey = true)
	private long guildId;
	@Column(comment = "玩家id", isKey = true)
	private long memberId;
	@Column(comment = "职位")
	private int job;

	/**默认的构造函数**/
	public GuildMemberDo(){}
	public GuildMemberDo(long guildId, long memberId){
		this.guildId = guildId;
		this.memberId = memberId;
	}

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
}
