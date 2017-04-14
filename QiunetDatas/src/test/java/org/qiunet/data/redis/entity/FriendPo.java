package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.redis.support.RedisEntity;

/**
 * @author qiunet
 *         Created on 17/1/5 08:42.
 */
@Alias("friendPo")
public class FriendPo extends RedisEntity {
	public static final String FIELD_UID = "uid";
	public static final String FIELD_FRIEND_DESCS = "friend_descs";
	
	private static final String [] fields = {FIELD_FRIEND_DESCS};
	private int uid;
	private String friend_descs;
	
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
