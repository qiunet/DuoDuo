package org.qiunet.function.base.basic;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.utils.scanner.anno.IgnoreEmptyWired;

/***
 * 基础功能接口
 *
 * @author qiunet
 * 2020-12-28 12:12
 */
@IgnoreEmptyWired
public interface IBasicFunction {

	/**
	 * 获得属性名
	 * @param attrName 属性名
	 * @param <Type> 枚举类型
	 * @return 枚举
	 */
	<Type extends Enum<Type> & IAttrEnum<Type>> Type parse(String attrName);

}
