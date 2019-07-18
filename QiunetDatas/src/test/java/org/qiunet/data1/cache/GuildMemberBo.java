package org.qiunet.data1.cache;

import org.qiunet.data1.support.IEntityBo;

public class GuildMemberBo implements IEntityBo<GuildMemberPo> {

	private GuildMemberPo guildMemberPo;

	GuildMemberBo(GuildMemberPo guildMemberPo) {
		this.guildMemberPo = guildMemberPo;
	}
	@Override
	public GuildMemberPo getPo() {
		return guildMemberPo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
