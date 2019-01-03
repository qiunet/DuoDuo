package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.RedisEntity;

/**
 * @author qiunet
 *         Created on 17/2/27 19:38.
 */
@Alias("GlobalTablePo")
public class GlobalTablePo extends RedisEntity {
	public enum FieldEnum implements IField { name }

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDbInfoKeyName() {
		return "id";
	}
	@Override
	public IField[] getFields() {
		return FieldEnum.values();
	}
}
