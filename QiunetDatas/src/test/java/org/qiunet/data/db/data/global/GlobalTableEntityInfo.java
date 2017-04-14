package org.qiunet.data.db.data.global;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.glboal.IdGlobalDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.GlobalTablePo;
import org.qiunet.data.redis.key.RedisKey;

/**
 * @author qiunet
 *         Created on 17/2/27 20:00.
 */
public class GlobalTableEntityInfo implements IEntityInfo<GlobalTablePo, GlobalTablePo> {
	@Override
	public String getNameSpace() {
		return "global_table";
	}

	@Override
	public Class<GlobalTablePo> getClazz() {
		return GlobalTablePo.class;
	}

	@Override
	public boolean needAsync() {
		return false;
	}

	@Override
	public GlobalTablePo getVo(GlobalTablePo globalTablePo) {
		return globalTablePo;
	}

	@Override
	public AbstractRedisUtil getRedisUtil() {
		return RedisDataUtil.getInstance();
	}

	@Override
	public Object getDbInfoKey(GlobalTablePo globalTablePo) {
		return globalTablePo.getId();
	}

	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.GLOBAL_TABLE.getAsyncKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(GlobalTablePo globalTablePo) {
		return getEntityDbInfo(globalTablePo.getId());
	}

	@Override
	public String getRedisKey(Object dbInfoKey) {
		return RedisKey.GLOBAL_TABLE.getKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(Object dbInfoKey) {
		return new IdGlobalDbInfo(dbInfoKey);
	}
}
