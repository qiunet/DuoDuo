package org.qiunet.template.login.info;

import redis.RedisDataUtil;
import org.qiunet.data.db.support.info.openidInfo.OpenidEntityDbInfo;
import redis.key.RedisKey;
import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.template.login.entity.LoginPo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.db.support.info.IEntityDbInfo;

public class LoginEntityInfo implements IEntityInfo<LoginPo, LoginPo> {
	
	@Override
	public String getNameSpace() {
		return "login";
	}
	
	@Override
	public Class<LoginPo> getClazz() {
      return LoginPo.class;
    }

    @Override
    public boolean needAsync() {
        return true;
    }

    @Override
    public LoginPo getVo(LoginPo po) {
       return po; 
    }

    @Override
    public AbstractRedisUtil getRedisUtil() {
      return RedisDataUtil.getInstance();
    }

    @Override
    public Object getDbInfoKey(LoginPo po) {
     return po.getOpenid();
    }

    @Override
    public String getAsyncKey(Object dbInfoKey) {
        return RedisKey.LOGIN.getAsyncKey(dbInfoKey);
    }

    @Override
    public IEntityDbInfo getEntityDbInfo(LoginPo po) {
        return getEntityDbInfo(getDbInfoKey(po));
    }
    
    @Override
    public IEntityDbInfo getEntityDbInfo(Object dbInfoKey) {
        return new OpenidEntityDbInfo(dbInfoKey);
    }

    @Override
    public String getRedisKey(Object dbInfoKey) {
        return RedisKey.LOGIN.getKey(dbInfoKey);
    }
}