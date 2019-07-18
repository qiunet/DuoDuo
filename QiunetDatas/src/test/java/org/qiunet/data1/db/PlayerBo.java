package org.qiunet.data1.db;

import org.qiunet.data1.support.IEntityBo;

public class PlayerBo implements IEntityBo<PlayerPo> {

	private PlayerPo playerPo;

	PlayerBo(PlayerPo playerPo) {
		this.playerPo = playerPo;
	}

	@Override
	public PlayerPo getPo() {
		return playerPo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
