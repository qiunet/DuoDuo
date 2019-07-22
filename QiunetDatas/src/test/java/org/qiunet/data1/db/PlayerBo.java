package org.qiunet.data1.db;

import org.qiunet.data1.support.IEntityBo;

public class PlayerBo implements IEntityBo<PlayerDo> {

	private PlayerDo playerDo;

	PlayerBo(PlayerDo playerDo) {
		this.playerDo = playerDo;
	}

	@Override
	public PlayerDo getDo() {
		return playerDo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
