package org.qiunet.data.util;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.redis.support.info.IRedisEntity;

import java.util.Date;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/1/4 16:52.
 */
public class TestPo extends BasePo implements IRedisEntity {

	private int level;

	private int exp;

	private Date regDt;

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
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
	public Map<String, String> getAllFeildsToHash() {
		return null;
	}

	@Override
	public String getDbInfoKeyName() {
		return null;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void setEntityDbInfo(IEntityDbInfo entityDbInfo) {

	}

	@Override
	public String getDbName() {
		return null;
	}

	@Override
	public int getDbIndex() {
		return 0;
	}

	@Override
	public String getDbSourceKey() {
		return null;
	}
}
