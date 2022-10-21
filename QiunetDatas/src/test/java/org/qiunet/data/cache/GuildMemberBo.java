package org.qiunet.data.cache;

import org.qiunet.data.support.IEntityBo;

public class GuildMemberBo implements IEntityBo<GuildMemberDo> {

	private final GuildMemberDo guildMemberDo;

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
