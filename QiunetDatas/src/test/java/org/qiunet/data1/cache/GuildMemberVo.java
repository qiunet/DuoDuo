package org.qiunet.data1.cache;

import org.qiunet.data1.support.IEntityVo;

public class GuildMemberVo implements IEntityVo<GuildMemberPo> {

	private GuildMemberPo guildMemberPo;

	GuildMemberVo(GuildMemberPo guildMemberPo) {
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
