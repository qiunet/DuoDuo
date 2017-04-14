package org.qiunet.data.db.data.login;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.openidInfo.OpenidEntityDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.key.RedisKey;

/**
 * @author qiunet
 *         Created on 17/2/14 12:01.
 */
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
	public LoginPo getVo(LoginPo loginPo) {
		return loginPo;
	}
	
	@Override
	public AbstractRedisUtil getRedisUtil() {
		return RedisDataUtil.getInstance();
	}
	
	@Override
	public Object getDbInfoKey(LoginPo loginPo) {
		return loginPo.getOpenid();
	}
	
	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.LOGIN.getAsyncKey(dbInfoKey);
	}
	
	@Override
	public IEntityDbInfo getEntityDbInfo(LoginPo loginPo) {
		return getEntityDbInfo(loginPo.getOpenid());
	}
	
	@Override
	public String getRedisKey(Object dbInfoKey) {
		return RedisKey.LOGIN.getKey(dbInfoKey);
	}
	
	@Override
	public IEntityDbInfo getEntityDbInfo(Object dbInfoKey) {
		return new OpenidEntityDbInfo((String) dbInfoKey);
	}
}
