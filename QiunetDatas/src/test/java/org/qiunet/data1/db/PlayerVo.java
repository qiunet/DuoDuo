package org.qiunet.data1.db;

import org.qiunet.data1.support.IEntityVo;

public class PlayerVo implements IEntityVo<PlayerPo> {

	private PlayerPo playerPo;

	public PlayerVo(PlayerPo playerPo) {
		this.playerPo = playerPo;
	}

	@Override
	public PlayerPo getPo() {
		return playerPo;
	}
}
