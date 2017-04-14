package org.qiunet.template.qunxiu.info;

import redis.RedisDataUtil;
import org.qiunet.data.db.support.info.idInfo.IdEntityDbInfo;
import redis.key.RedisKey;
import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.template.qunxiu.entity.QunxiuPo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.db.support.info.IEntityDbInfo;

public class QunxiuEntityInfo implements IEntityInfo<QunxiuPo, QunxiuPo> {
	
	@Override
	public String getNameSpace() {
		return "qunxiu";
	}
	
	@Override
	public Class<QunxiuPo> getClazz() {
      return QunxiuPo.class;
    }

    @Override
    public boolean needAsync() {
        return true;
    }

    @Override
    public QunxiuPo getVo(QunxiuPo po) {
       return po; 
    }

    @Override
    public AbstractRedisUtil getRedisUtil() {
      return RedisDataUtil.getInstance();
    }

    @Override
    public Object getDbInfoKey(QunxiuPo po) {
     return po.getId();
    }

    @Override
    public String getAsyncKey(Object dbInfoKey) {
        return RedisKey.QUNXIU.getAsyncKey(dbInfoKey);
    }

    @Override
    public IEntityDbInfo getEntityDbInfo(QunxiuPo po) {
        return getEntityDbInfo(getDbInfoKey(po));
    }
    
    @Override
    public IEntityDbInfo getEntityDbInfo(Object dbInfoKey) {
        return new IdEntityDbInfo(dbInfoKey);
    }

    @Override
    public String getRedisKey(Object dbInfoKey) {
        return RedisKey.QUNXIU.getKey(dbInfoKey);
    }
}