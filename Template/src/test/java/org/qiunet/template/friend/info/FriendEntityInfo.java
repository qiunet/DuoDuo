package org.qiunet.template.friend.info;

import redis.RedisDataUtil;
import org.qiunet.data.db.support.info.uidinfo.UidEntityDbInfo;
import redis.key.RedisKey;
import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.template.friend.entity.FriendVo;
import org.qiunet.template.friend.entity.FriendPo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.db.support.info.IEntityDbInfo;

public class FriendEntityInfo implements IEntityInfo<FriendPo, FriendVo> {
	
	@Override
	public String getNameSpace() {
		return "friend";
	}
	
	@Override
	public Class<FriendPo> getClazz() {
      return FriendPo.class;
    }

    @Override
    public boolean needAsync() {
        return true;
    }

    @Override
    public FriendVo getVo(FriendPo po) {
        return new FriendVo(po);
    }

    @Override
    public AbstractRedisUtil getRedisUtil() {
      return RedisDataUtil.getInstance();
    }

    @Override
    public Object getDbInfoKey(FriendPo po) {
     return po.getUid();
    }

    @Override
    public String getAsyncKey(Object dbInfoKey) {
        return RedisKey.FRIEND.getAsyncKey(dbInfoKey);
    }

    @Override
    public IEntityDbInfo getEntityDbInfo(FriendPo po) {
        return getEntityDbInfo(getDbInfoKey(po));
    }
    
    @Override
    public IEntityDbInfo getEntityDbInfo(Object dbInfoKey) {
        return new UidEntityDbInfo(dbInfoKey);
    }

    @Override
    public String getRedisKey(Object dbInfoKey) {
        return RedisKey.FRIEND.getKey(dbInfoKey);
    }
}