package org.qiunet.data.db.data.friend;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.uidinfo.UidEntityDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.FriendPo;
import org.qiunet.data.redis.key.RedisKey;

/**
 * @author qiunet
 *         Created on 17/2/12 15:43.
 */
public class FriendEntityInfo implements IEntityInfo<Integer, FriendPo, FriendVo> {
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
	public FriendVo getVo(FriendPo friendPo) {
		return new FriendVo(friendPo);
	}

	@Override
	public AbstractRedisUtil getRedisUtil() {
		return RedisDataUtil.getInstance();
	}
	@Override
	public Integer getDbInfoKey(FriendPo friendPo) {
		return friendPo.getUid();
	}
	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.FRIEND.getAsyncKey(dbInfoKey);
	}

	@Override
	public String getRedisKey(Object dbInfoKey) {
		return RedisKey.FRIEND.getKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(Integer dbInfoKey) {
		return new UidEntityDbInfo(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(FriendPo friendPo) {
		return getEntityDbInfo(friendPo.getUid());
	}
}
