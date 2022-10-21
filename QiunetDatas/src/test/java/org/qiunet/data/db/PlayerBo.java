package org.qiunet.data.db;

import org.qiunet.data.db.loader.DbEntityBo;

public class PlayerBo extends DbEntityBo<PlayerDo> {

	private final PlayerDo playerDo;

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
