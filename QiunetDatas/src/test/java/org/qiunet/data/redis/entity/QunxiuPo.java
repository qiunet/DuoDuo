package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.RedisEntity;
/**
 * @author qiunet
 *         Created on 17/1/5 08:42.
 */
@Alias("qunxiuPo")
public class QunxiuPo extends RedisEntity {

	public enum FieldEnum implements IField {
		master,
		level,
		name
	}

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
	public IField[] getFields() {
		return QunxiuPo.FieldEnum.values();
	}
	@Override
	public String getDbInfoKeyName() {
		return "id";
	}
}
