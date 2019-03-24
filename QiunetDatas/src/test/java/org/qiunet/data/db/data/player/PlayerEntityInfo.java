package org.qiunet.data.db.data.player;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.uidinfo.UidEntityDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.PlayerPo;
import org.qiunet.data.redis.key.RedisKey;

/**
 * @author qiunet
 *         Created on 17/2/13 13:36.
 */
public class PlayerEntityInfo implements IEntityInfo<Integer, PlayerPo, PlayerVo> {
	@Override
	public String getNameSpace() {
		return "player";
	}

	@Override
	public Class<PlayerPo> getClazz() {
		return PlayerPo.class;
	}

	@Override
	public boolean needAsync() {
		return true;
	}

	@Override
	public PlayerVo getVo(PlayerPo playerPo) {
		return new PlayerVo(playerPo);
	}

	@Override
	public AbstractRedisUtil getRedisUtil() {
		return RedisDataUtil.getInstance();
	}

	@Override
	public Integer getDbInfoKey(PlayerPo playerPo) {
		return playerPo.getUid();
	}

	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.PLAYER.getAsyncKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(PlayerPo playerPo) {
		return getEntityDbInfo(playerPo.getUid());
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(Integer dbInfoKey) {
		return new UidEntityDbInfo(dbInfoKey);
	}

	@Override
	public String getRedisKey(Object dbInfoKey) {
		return RedisKey.PLAYER.getKeyByParams(dbInfoKey);
	}
}
