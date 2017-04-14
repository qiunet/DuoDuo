package org.qiunet.data.redis.entity;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.redis.support.RedisList;

/**
 * @author qiunet
 *         Created on 17/2/12 13:04.
 */
@Alias("SysmsgPo")
public class SysmsgPo extends RedisList{
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_UID = "uid";
	public static final String FIELD_MSG = "msg";
	public static final String FIELD_PARAM = "param";
	
	private static final String [] fields = {FIELD_MSG, FIELD_PARAM};
	
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
		return FIELD_UID;
	}
	
	@Override
	public String getSubKey() {
		return FIELD_ID;
	}
	
	@Override
	public int getSubId() {
		return id;
	}
	
	@Override
	protected String[] getFields() {
		return fields;
	}
}
