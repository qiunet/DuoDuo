package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.redis.support.PlatformRedisList;

/**
 * @author qiunet
 *         Created on 17/1/5 08:41.
 */
@Alias("equipPo")
public class EquipPo extends PlatformRedisList {
	public static final String FILED_ID = "id";
	public static final String FILED_UID = "uid";
	public static final String FILED_LEVEL = "level";
	public static final String FILED_EXP = "exp";
	
	private static final String [] fields = {FILED_EXP, FILED_LEVEL};
	
	private int id;
	private int uid;
	private int level;
	private int exp;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
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
	public String getSubKey() {
		return FILED_ID;
	} 
	@Override
	public String getDbInfoKeyName() {
		return FILED_UID;
	}
	@Override
	public int getSubId() {
		return id;
	}
}
