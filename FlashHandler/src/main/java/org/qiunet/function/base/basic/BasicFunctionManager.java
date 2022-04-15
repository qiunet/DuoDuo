package org.qiunet.function.base.basic;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.base.IResourceCfg;
import org.qiunet.utils.scanner.anno.AutoWired;

/***
 *
 * @Author qiunet
 * @Date 2021/1/9 22:03
 **/
public enum BasicFunctionManager implements IResourceFunction, IAttrFunction {
	instance;
	@AutoWired
	private static IBasicFunction basicFunction;

	@Override
	public <Type extends Enum<Type> & IAttrEnum<Type>> Type parse(String attrName) {
		return basicFunction.parse(attrName);
	}

	@Override
	public <T extends IResourceCfg> T getResById(String cfgId) {
		return basicFunction.getResById(cfgId);
	}
}
