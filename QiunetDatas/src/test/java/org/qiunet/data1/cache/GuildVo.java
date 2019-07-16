package org.qiunet.data1.cache;

import org.qiunet.data1.support.IEntityVo;

public class GuildVo implements IEntityVo<GuildPo> {
	private GuildPo guildPo;

	public GuildVo(GuildPo guildPo) {
		this.guildPo = guildPo;
	}
	@Override
	public GuildPo getPo() {
		return guildPo;
	}
}
