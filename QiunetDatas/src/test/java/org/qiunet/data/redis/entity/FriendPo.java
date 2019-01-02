package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.RedisEntity;

/**
 * @author qiunet
 *         Created on 17/1/5 08:42.
 */
@Alias("friendPo")
public class FriendPo extends RedisEntity {
	public enum FieldEnum implements IField {
		FRIEND_DESCS("friend_descs"),
		;
		private String fieldName;
		FieldEnum(String fieldName) {
			this.fieldName = fieldName;
		}
		@Override
		public String getFieldName() {
			return fieldName;
		}
	}
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
	public IField[] getFields() {
		return FieldEnum.values();
	}
	@Override
	public String getDbInfoKeyName() {
		return "uid";
	}
}
