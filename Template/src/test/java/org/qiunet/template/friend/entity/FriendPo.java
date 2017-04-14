package org.qiunet.template.friend.entity;

import org.qiunet.data.redis.support.RedisEntity;
import org.apache.ibatis.type.Alias;
/**
 * 对象为自动创建
 */
@Alias("FriendPo")
public class FriendPo extends RedisEntity {
    public static final String FIELD_UID = "uid";
    public static final String FIELD_FRIEND_DESCS = "friend_descs";
    private static final String [] fields = {FIELD_FRIEND_DESCS };
    private int uid;                             /** uid   */
    private String friend_descs;                 /** 好友的字符串  */
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getFriend_descs() {
        return friend_descs;
    }
    public void setFriend_descs(String friend_descs) {
        this.friend_descs = friend_descs;
    }
    @Override
    protected String[] getFields() {
        return fields;
    }
    @Override
    public String getDbInfoKeyName() {
        return FIELD_UID;
    }
}
