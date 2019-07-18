package org.qiunet.data1.cache;

import org.qiunet.data1.support.IEntityBo;

public class GuildBo implements IEntityBo<GuildPo> {
	private GuildPo guildPo;

	GuildBo(GuildPo guildPo) {
		this.guildPo = guildPo;
	}
	@Override
	public GuildPo getPo() {
		return guildPo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
