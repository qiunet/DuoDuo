package org.qiunet.test.function.test.formula;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.base.basic.IBasicFunction;

/***
 *
 *
 * @author qiunet
 * 2020-12-30 15:03
 */
public class BasicFunction implements IBasicFunction {

	@Override
	public <Type extends Enum<Type> & IAttrEnum<Type>> Type parse(String attrName) {
		return (Type) FightAttrType.valueOf(attrName);
	}
}
