package org.qiunet.data.redis.entity;


import org.qiunet.data.core.support.entityInfo.IField;

/**
 * @author qiunet
 *         Created on 17/1/5 11:52.
 */
public class EquipCopyPo extends EquipPo {
	public enum FieldEnum implements IField {
		exp,
		star,
		level
	}
	private int star;
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	@Override
	public IField[] getFields() {
		return FieldEnum.values();
	}
}
