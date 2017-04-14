package org.qiunet.data.redis.entity;

import org.qiunet.utils.common.CommonUtil;

import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/1/5 11:52.
 */
public class EquipCopyPo extends EquipPo {
	
	public static final String FILED_LEVEL = "level";
	public static final String FILED_EXP = "exp";
	public static final String FILED_STAR = "star";
	
	private static final String [] fields = {FILED_EXP, FILED_LEVEL, FILED_STAR};
	private int star;
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	@Override
	protected String[] getFields() {
		return fields;
	}
}
