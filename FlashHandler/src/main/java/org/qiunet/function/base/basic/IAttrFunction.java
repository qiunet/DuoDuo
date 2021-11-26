package org.qiunet.function.base.basic;

import org.qiunet.function.attr.enums.IAttrEnum;

/***
 *
 * @Author qiunet
 * @Date 2021/1/9 22:03
 **/
public interface IAttrFunction {

	/**
	 * 获得属性名
	 * @param attrName
	 * @param <Type>
	 * @return
	 */
	<Type extends Enum<Type> & IAttrEnum<Type>> Type parse(String attrName);
}
