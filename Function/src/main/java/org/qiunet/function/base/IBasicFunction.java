package org.qiunet.function.base;

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
	 * 获得指定资源的subType
	 * @param cfgId 资源id
	 * @return subType
	 */
	IResourceSubType getResSubType(int cfgId);

	/**
	 * 获得属性名
	 * @param attrName
	 * @param <Type>
	 * @return
	 */
	<Type extends Enum<Type> & IAttrEnum<Type>> Type parse(String attrName);
}
