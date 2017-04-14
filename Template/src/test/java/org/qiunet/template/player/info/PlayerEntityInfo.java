package org.qiunet.template.player.info;

import redis.RedisDataUtil;
import org.qiunet.data.db.support.info.uidinfo.UidPlatformEntityDbInfo;
import redis.key.RedisKey;
import org.qiunet.data.core.support.entityInfo.IPlatformEntityInfo;
import org.qiunet.template.player.entity.PlayerVo;
import org.qiunet.template.player.entity.PlayerPo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;   
import org.qiunet.data.enums.PlatformType;

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
    public PlayerVo getVo(PlayerPo po) {
        return new PlayerVo(po);
    }

    @Override
    public AbstractRedisUtil getRedisUtil() {
      return RedisDataUtil.getInstance();
    }

    @Override
    public Object getDbInfoKey(PlayerPo po) {
     return po.getUid();
    }

    @Override
    public String getAsyncKey(Object dbInfoKey) {
        return RedisKey.PLAYER.getAsyncKey(dbInfoKey);
    }

    @Override
    public IEntityDbInfo getEntityDbInfo(PlayerPo po) {
        return getEntityDbInfo(getDbInfoKey(po), po.getPlatform());
    }
    
    @Override
    public IPlatformEntityDbInfo getEntityDbInfo(Object dbInfoKey, PlatformType platform) {
         return new UidPlatformEntityDbInfo(dbInfoKey, platform);
    }

    @Override
    public String getRedisKey(Object dbInfoKey, PlatformType platform) {
        return RedisKey.PLAYER.getKey(dbInfoKey, platform);
    }
}