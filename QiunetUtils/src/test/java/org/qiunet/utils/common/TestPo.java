package org.qiunet.utils.common;

import java.util.Date;

/**
 * @author qiunet
 *         Created on 17/1/4 16:52.
 */
public class TestPo extends BasePo {
	
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
}
