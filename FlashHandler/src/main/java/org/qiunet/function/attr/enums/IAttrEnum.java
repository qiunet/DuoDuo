package org.qiunet.function.attr.enums;

/***
 * 属性的枚举接口
 *
 * @author qiunet
 * 2020-11-16 10:05
 */
public interface IAttrEnum<AttrEnum extends Enum<AttrEnum> & IAttrEnum<AttrEnum>> {
	/**
	 * 类型int值
	 * @return
	 */
	int type();

	/**
	 * 描述
	 * @return
	 */
	String desc();
	/**
	 * 枚举名.
	 * @return
	 */
	String name();
	/**
	 * 属性值类型
	 * @return
	 */
	AttrValueType valueType();
	/**
	 * 对其它属性的加成
	 * @return
	 */
	AttrEnum[] additions();
}
