package org.qiunet.data1.cache;

import org.qiunet.data1.support.IEntityBo;

public class GuildBo implements IEntityBo<GuildDo> {
	private GuildDo guildDo;

	GuildBo(GuildDo guildDo) {
		this.guildDo = guildDo;
	}
	@Override
	public GuildDo getDo() {
		return guildDo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
