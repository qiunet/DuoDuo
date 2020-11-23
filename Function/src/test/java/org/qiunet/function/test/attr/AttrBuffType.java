package org.qiunet.function.test.attr;

import org.qiunet.function.attr.buff.IAttrBuff;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 10:34
 */
public enum AttrBuffType implements IAttrBuff {
	allEquipBaseRct("装备基础属性万分比加成"),
	EquipGemLegRct("装备镶嵌加成"),

	;
	private String desc;

	AttrBuffType(String desc) {
		this.desc = desc;
	}

	@Override
	public String desc() {
		return desc;
	}
}
