package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.redis.support.RedisEntity;
/**
 * @author qiunet
 *         Created on 17/1/5 08:42.
 */
@Alias("qunxiuPo")
public class QunxiuPo extends RedisEntity {
	public static final String FIELD_ID = "id" ;
	public static final String FIELD_NAME = "name" ;
	public static final String FIELD_MASTER = "master" ;
	public static final String FIELD_LEVEL = "level" ;
	private static final String [] fields = {FIELD_MASTER, FIELD_NAME, FIELD_LEVEL};
	private int id;
	private String name;
	private int master;
	private int level;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMaster() {
		return master;
	}
	
	public void setMaster(int master) {
		this.master = master;
	}
	@Override
	protected String[] getFields() {
		return fields;
	}
	@Override
	public String getDbInfoKeyName() {
		return FIELD_ID;
	}
}
