package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.RedisList;

/**
 * @author qiunet
 *         Created on 17/2/12 13:04.
 */
@Alias("SysmsgPo")
public class SysmsgPo extends RedisList{
	public enum FieldEnum implements IField {
		MSG("msg"),
		PARAM("param"),
		;
		private String fieldName;
		FieldEnum(String fieldName) {
			this.fieldName = fieldName;
		}
		@Override
		public String getName() {
			return fieldName;
		}
	}

	private int id;
	private int uid;
	private String msg;
	private String param;

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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public String getDbInfoKeyName() {
		return "uid";
	}

	@Override
	public String getSubKey() {
		return "id";
	}

	@Override
	public Integer getSubId() {
		return id;
	}

	@Override
	public IField[] getFields() {
		return FieldEnum.values();
	}
}
