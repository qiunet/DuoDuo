package org.qiunet.data.db.data.player;

import org.qiunet.data.core.support.entityInfo.IPlatformEntityInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;
import org.qiunet.data.db.support.info.uidinfo.UidPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.PlayerPo;
import org.qiunet.data.redis.key.RedisKey;

import java.util.Objects;

/**
 * @author qiunet
 *         Created on 17/2/13 13:36.
 */
public class PlayerEntityInfo implements IPlatformEntityInfo<PlayerPo, PlayerVo> {
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
	public Object getDbInfoKey(PlayerPo playerPo) {
		return playerPo.getUid();
	}

	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.PLAYER.getAsyncKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(PlayerPo playerPo) {
		return getEntityDbInfo(playerPo.getUid(), playerPo.getPlatform());
	}

	@Override
	public IPlatformEntityDbInfo getEntityDbInfo(Object dbInfoKey, PlatformType platform) {
		return new UidPlatformEntityDbInfo((Integer) dbInfoKey, platform);
	}

	@Override
	public String getRedisKey(Object dbInfoKey, PlatformType platform) {
		return RedisKey.PLAYER.getKeyByParams(dbInfoKey, platform);
	}
}
