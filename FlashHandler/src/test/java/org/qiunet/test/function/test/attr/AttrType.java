package org.qiunet.test.function.test.attr;

import org.qiunet.function.attr.enums.AttrValueType;
import org.qiunet.function.attr.enums.IAttrEnum;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 11:01
 */
public enum AttrType implements IAttrEnum<AttrType> {
	ATK(1, "攻"),
	DEF(2, "防"),
	HP(3, "血"),
	R_HP(4, "血加成", AttrValueType.rct, AttrType.HP),

	;

	private final int type;
	private final String desc;
	private final AttrType [] additions;
	private final AttrValueType valueType;

	AttrType(int type, String desc) {
		this(type, desc, AttrValueType.base);
	}

	AttrType(int type, String desc, AttrValueType valueType, AttrType... additions) {
		this.type = type;
		this.desc = desc;
		this.additions = additions;
		this.valueType = valueType;
	}

	@Override
	public String desc() {
		return desc;
	}

	@Override
	public int type() {
		return type;
	}

	@Override
	public AttrValueType valueType() {
		return valueType;
	}

	@Override
	public AttrType[] additions() {
		return additions;
	}
}
