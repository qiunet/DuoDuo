package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.redis.support.PlatformRedisEntity;

/**
 * @author qiunet
 *         Created on 17/1/5 08:41.
 */
@Alias("playerPo")
public class PlayerPo extends PlatformRedisEntity {
	public static final String FILED_UID = "uid";
	public static final String FILED_LEVEL = "level";
	public static final String FILED_EXP = "exp";
	
	private static final String [] fields = {FILED_EXP, FILED_LEVEL}; 
	private int uid;
	private int level;
	private int exp;
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getExp() {
		return exp;
	}
	
	public void setExp(int exp) {
		this.exp = exp;
	}
	
	@Override
	protected String[] getFields() {
		return fields;
	}
	
	@Override
	public String getDbInfoKeyName() {
		return FILED_UID;
	}
}
