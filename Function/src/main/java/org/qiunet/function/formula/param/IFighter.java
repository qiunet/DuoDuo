package org.qiunet.function.formula.param;

import org.qiunet.function.attr.enums.IAttrEnum;
/***
 * 玩家战斗单元
 *
 * @author qiunet
 * 2020-12-29 20:52
 */
public interface IFighter<AttrEnum extends Enum<AttrEnum> & IAttrEnum<AttrEnum>> {
	/**
	 * 战斗单元的id
	 * @return id
	 */
	int getId();
	/**
	 * 获得属性值
	 * @param attrType
	 * @return
	 */
	long getAttr(AttrEnum attrType);
}
