package org.qiunet.template.player.entity;

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
