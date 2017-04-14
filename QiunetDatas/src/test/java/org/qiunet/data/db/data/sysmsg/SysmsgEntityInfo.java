package org.qiunet.data.db.data.sysmsg;

import org.qiunet.data.core.support.entityInfo.IEntityListInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.db.support.info.uidinfo.UidEntityListDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.SysmsgPo;
import org.qiunet.data.redis.key.RedisKey;
import org.qiunet.utils.data.CommonData;

/**
 * @author qiunet
 *         Created on 17/2/13 16:10.
 */
public class SysmsgEntityInfo implements IEntityListInfo<SysmsgPo, SysmsgVo> {
	@Override
	public String getNameSpace() {
		return "sysmsg";
	}
	
	@Override
	public Class<SysmsgPo> getClazz() {
		return SysmsgPo.class;
	}
	
	@Override
	public boolean needAsync() {
		return true;
	}
	
	@Override
	public SysmsgVo getVo(SysmsgPo sysMsgPo) {
		return new SysmsgVo(sysMsgPo);
	}
	
	@Override
	public AbstractRedisUtil getRedisUtil() {
		return RedisDataUtil.getInstance();
	}
	
	@Override
	public Object getDbInfoKey(SysmsgPo sysMsgPo) {
		return sysMsgPo.getUid();
	}
	
	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.SYSMSG.getAsyncKey(dbInfoKey);
	}
	
	@Override
	public Integer getSubKey(SysmsgPo sysMsgPo) {
		return sysMsgPo.getSubId();
	}
	
	@Override
	public String getRedisKey(Object dbInfoKey) {
		return RedisKey.SYSMSG.getKey(dbInfoKey);
	}
	
	@Override
	public IEntityDbInfo getEntityDbInfo(SysmsgPo sysMsgPo) {
		return getEntityDbInfo(sysMsgPo.getUid(), getSubKey(sysMsgPo));
	}
	
	@Override
	public IEntityListDbInfo getEntityDbInfo(Object dbInfoKey, int subId) {
		return new UidEntityListDbInfo((Integer) dbInfoKey, subId);
	}
}
