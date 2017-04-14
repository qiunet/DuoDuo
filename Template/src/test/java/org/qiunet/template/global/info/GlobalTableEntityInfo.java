package org.qiunet.template.global.info;

import redis.RedisDataUtil;
import org.qiunet.data.db.support.info.glboal.IdGlobalDbInfo;
import redis.key.RedisKey;
import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.template.global.entity.GlobalTablePo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.db.support.info.IEntityDbInfo;

public class GlobalTableEntityInfo implements IEntityInfo<GlobalTablePo, GlobalTablePo> {
	
	@Override
	public String getNameSpace() {
		return "globaltable";
	}
	
	@Override
	public Class<GlobalTablePo> getClazz() {
      return GlobalTablePo.class;
    }

    @Override
    public boolean needAsync() {
        return true;
    }

    @Override
    public GlobalTablePo getVo(GlobalTablePo po) {
       return po; 
    }

    @Override
    public AbstractRedisUtil getRedisUtil() {
      return RedisDataUtil.getInstance();
    }

    @Override
    public Object getDbInfoKey(GlobalTablePo po) {
     return po.getId();
    }

    @Override
    public String getAsyncKey(Object dbInfoKey) {
        return RedisKey.GLOBALTABLE.getAsyncKey(dbInfoKey);
    }

    @Override
    public IEntityDbInfo getEntityDbInfo(GlobalTablePo po) {
        return getEntityDbInfo(getDbInfoKey(po));
    }
    
    @Override
    public IEntityDbInfo getEntityDbInfo(Object dbInfoKey) {
        return new IdGlobalDbInfo(dbInfoKey);
    }

    @Override
    public String getRedisKey(Object dbInfoKey) {
        return RedisKey.GLOBALTABLE.getKey(dbInfoKey);
    }
}