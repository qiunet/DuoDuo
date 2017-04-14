package org.qiunet.data.db.dao;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.datasource.CustomerContextHolder;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.uidinfo.UidPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.entity.PlayerPo;

/**
 * @author qiunet
 *         Created on 17/1/24 16:05.
 */
public class PlayerDaoImpl implements PlayerDao {
	private static PlayerDao instance;
	private PlayerDaoImpl(){
		instance = this;
	}
	public static PlayerDao getInstance(){
		if (instance == null) {
			instance = new PlayerDaoImpl();
		}
		return instance;
	}
	@Override
	public PlayerPo getPlayerPo(int uid, PlatformType platformType) {
		IEntityDbInfo dbInfo = new UidPlatformEntityDbInfo(uid, platformType);
		CustomerContextHolder.setCustomerType(dbInfo.getDbSourceType());
		return DatabaseSupport.getInstance().selectOne("player.getPlayerPo", dbInfo);
	}
	
	@Override
	public void insertPlayerPo(PlayerPo playerPo) {
		IEntityDbInfo dbInfo = new UidPlatformEntityDbInfo(playerPo.getUid(), playerPo.getPlatform());
		playerPo.setEntityDbInfo(dbInfo);
		DatabaseSupport.getInstance().insert("player.insertPlayerPo", playerPo);
	}
	
	@Override
	public void deletePlayerPo(PlayerPo playerPo) {
		IEntityDbInfo dbInfo = new UidPlatformEntityDbInfo(playerPo.getUid(), playerPo.getPlatform());
		playerPo.setEntityDbInfo(dbInfo);
		DatabaseSupport.getInstance().delete("player.deletePlayerPo", playerPo);
	}
}
