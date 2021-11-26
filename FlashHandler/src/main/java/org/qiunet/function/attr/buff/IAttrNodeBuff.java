package org.qiunet.function.attr.buff;

import org.qiunet.function.attr.enums.IAttrEnum;

/***
 * AttrNode buff类型对象的接口.
 *
 * @author qiunet
 * 2020-11-16 16:39
 */
public interface IAttrNodeBuff<Attr extends Enum<Attr> & IAttrEnum<Attr>, Buff extends IAttrNodeBuff> {
	/**
	 * 是否相等.
	 * @param buff
	 * @return
	 */
	boolean equals(Buff buff);

	/**
	 * 返回value计算后的值
	 * @param attrType
	 * @param value
	 * @return
	 */
	long calBuffVal(Attr attrType, long value);
}
