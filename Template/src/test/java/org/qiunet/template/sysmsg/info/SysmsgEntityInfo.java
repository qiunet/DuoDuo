package org.qiunet.template.sysmsg.info;

import redis.RedisDataUtil;
import org.qiunet.data.db.support.info.uidinfo.UidEntityListDbInfo;
import redis.key.RedisKey;
import org.qiunet.data.core.support.entityInfo.IEntityListInfo;
import org.qiunet.template.sysmsg.entity.SysmsgVo;
import org.qiunet.template.sysmsg.entity.SysmsgPo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IEntityListDbInfo;   

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
    public SysmsgVo getVo(SysmsgPo po) {
        return new SysmsgVo(po);
    }

    @Override
    public AbstractRedisUtil getRedisUtil() {
      return RedisDataUtil.getInstance();
    }

    @Override
    public Object getDbInfoKey(SysmsgPo po) {
     return po.getUid();
    }

    @Override
    public String getAsyncKey(Object dbInfoKey) {
        return RedisKey.SYSMSG.getAsyncKey(dbInfoKey);
    }

    @Override
    public Integer getSubKey(SysmsgPo po) {
        return po.getSubId();
    }
    
    @Override
    public IEntityDbInfo getEntityDbInfo(SysmsgPo po) {
        return getEntityDbInfo(getDbInfoKey(po), getSubKey(po));
    }
    
    @Override
    public IEntityListDbInfo getEntityDbInfo(Object dbInfoKey, int subId) {
         return new UidEntityListDbInfo(dbInfoKey, subId);
    }

    @Override
    public String getRedisKey(Object dbInfoKey) {
        return RedisKey.SYSMSG.getKey(dbInfoKey);
    }
}