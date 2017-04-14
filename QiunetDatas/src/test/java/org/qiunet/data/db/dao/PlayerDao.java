package org.qiunet.data.db.dao;

import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.entity.PlayerPo;

/**
 * @author qiunet
 *         Created on 17/1/24 16:02.
 */
public interface PlayerDao {
	
	public PlayerPo getPlayerPo(int uid, PlatformType platformType);
	
	public void insertPlayerPo(PlayerPo playerPo);
	
	public void deletePlayerPo(PlayerPo playerPo);
}
