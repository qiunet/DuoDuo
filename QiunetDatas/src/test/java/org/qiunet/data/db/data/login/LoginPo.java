package org.qiunet.data.db.data.login;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.redis.support.RedisEntity;

/**
 * @author qiunet
 *         Created on 17/2/14 11:58.
 */
@Alias("loginPo")
public class LoginPo extends RedisEntity {
	public static final String FIELD_OPENID = "openid";
	public static final String FIELD_UID = "uid";
	public static final String FIELD_TOKEN = "token";
	
	private static final String [] fields = {FIELD_UID, FIELD_TOKEN};
	
	private String openid;
	private int uid;
	private String token;
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String getDbInfoKeyName() {
		return FIELD_OPENID;
	}
	
	@Override
	public String[] getFields() {
		return fields;
	}
}
