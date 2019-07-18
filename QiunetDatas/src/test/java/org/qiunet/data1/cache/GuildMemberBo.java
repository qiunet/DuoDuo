package org.qiunet.data1.cache;

import org.qiunet.data1.support.IEntityBo;

public class GuildMemberBo implements IEntityBo<GuildMemberDo> {

	private GuildMemberDo guildMemberDo;

	GuildMemberBo(GuildMemberDo guildMemberDo) {
		this.guildMemberDo = guildMemberDo;
	}
	@Override
	public GuildMemberDo getDo() {
		return guildMemberDo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
