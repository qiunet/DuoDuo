package org.qiunet.data.db.dao;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.uidinfo.UidEntityDbInfo;
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
	public PlayerPo getPlayerPo(int uid) {
		IEntityDbInfo dbInfo = new UidEntityDbInfo(uid);
		return DatabaseSupport.getInstance().selectOne(dbInfo.getDbSourceKey(),"player.getPlayerPo", dbInfo);
	}

	@Override
	public void insertPlayerPo(PlayerPo playerPo) {
		IEntityDbInfo dbInfo = new UidEntityDbInfo(playerPo.getUid());
		playerPo.setEntityDbInfo(dbInfo);
		DatabaseSupport.getInstance().insert(dbInfo.getDbSourceKey(),"player.insertPlayerPo", playerPo);
	}

	@Override
	public void deletePlayerPo(PlayerPo playerPo) {
		IEntityDbInfo dbInfo = new UidEntityDbInfo(playerPo.getUid());
		playerPo.setEntityDbInfo(dbInfo);
		DatabaseSupport.getInstance().delete(dbInfo.getDbSourceKey(),"player.deletePlayerPo", playerPo);
	}
}
