package org.qiunet.data.db.data.player;

import org.qiunet.data.db.dao.PlayerDao;
import org.qiunet.data.redis.entity.PlayerPo;

/**
 * @author qiunet
 *         Created on 17/2/13 12:29.
 */
public class PlayerVo {
	private PlayerPo playerPo;
	
	public PlayerVo(PlayerPo playerPo) {
		this.playerPo = playerPo;
	}
	
	public PlayerPo getPlayerPo() {
		return playerPo;
	}
}
