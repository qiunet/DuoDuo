package org.qiunet.template.login.entity;

import org.qiunet.data.redis.support.RedisEntity;
import org.apache.ibatis.type.Alias;
/**
 * 对象为自动创建
 */
@Alias("LoginPo")
public class LoginPo extends RedisEntity {
    public static final String FIELD_OPENID = "openid";
    public static final String FIELD_UID = "uid";
    public static final String FIELD_TOKEN = "token";
    private static final String [] fields = {FIELD_UID ,FIELD_TOKEN };
    private String openid;                       /** openId  */
    private int uid;                             /** uid   */
    private String token;                        /** openID 对应的 token  */
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
    protected String[] getFields() {
        return fields;
    }
    @Override
    public String getDbInfoKeyName() {
        return FIELD_OPENID;
    }
}
