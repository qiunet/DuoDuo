package org.qiunet.test.function.test.attr.equip;

import org.qiunet.function.attr.tree.IAttrNodeType;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 17:0
 */
public enum EquipAttrNode implements IAttrNodeType {
	EQUIP_ROOT(null, "装备根节点"),
	BASE(null, "装备基础属性父节点"),
	EQUIP_BASE_POSITION(EquipPostion.class, "装备基础属性"),
	GEM(null, "装备部位镶嵌宝石父节点"),
	GEM_POSITION(EquipPostion.class, "装备部位镶嵌宝石属性"),
	;

	private final Class<?> keyClass;
	private final String desc;

	EquipAttrNode(Class<?> keyClass, String desc) {
		this.keyClass = keyClass;
		this.desc = desc;
	}

	@Override
	public Class<?> keyClass() {
		return keyClass;
	}

	@Override
	public String desc() {
		return desc;
	}
}
