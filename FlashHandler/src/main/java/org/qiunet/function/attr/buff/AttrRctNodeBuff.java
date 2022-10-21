package org.qiunet.function.attr.buff;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.utils.math.MathUtil;

/***
 * 给属性加万分比
 * 可以自己继承接口. 实现自定义的功能.
 *
 * @author qiunet
 * 2020-11-20 10:56
 */
public class AttrRctNodeBuff<Attr extends Enum<Attr> & IAttrEnum<Attr>> implements IAttrNodeBuff<Attr, AttrRctNodeBuff> {
	/**
	 * 加成的万分比
	 */
	private final int rct;

	public AttrRctNodeBuff(int rct) {
		this.rct = rct;
	}

	@Override
	public boolean equals(AttrRctNodeBuff buff) {
		return rct == buff.rct;
	}

	@Override
	public long calBuffVal(Attr attrType, long value) {
		return MathUtil.getByRate(value, rct);
	}
}
