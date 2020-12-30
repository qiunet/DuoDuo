package org.qiunet.function.test.formula;

import org.qiunet.function.attr.enums.AttrValueType;
import org.qiunet.function.attr.enums.IAttrEnum;

/***
 *
 *
 * @author qiunet
 * 2020-12-30 15:00
 */
public enum FightAttrType implements IAttrEnum<FightAttrType> {
	ATT (1, "攻击力"),
	DEF (2, "防御力"),
	;
	private int type;
	private String desc;

	FightAttrType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Override
	public int type() {
		return type;
	}

	@Override
	public String desc() {
		return desc;
	}

	@Override
	public AttrValueType valueType() {
		return null;
	}

	@Override
	public FightAttrType[] additions() {
		return new FightAttrType[0];
	}
}
