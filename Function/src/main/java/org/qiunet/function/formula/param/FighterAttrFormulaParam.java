package org.qiunet.function.formula.param;

import org.qiunet.function.attr.enums.IAttrEnum;

/***
 * 战斗中属性的公式
 * 运用: self.MAX_HP
 *      target.ATT
 *
 * @author qiunet
 * 2020-12-30 12:49
 */
public class FighterAttrFormulaParam<Type extends Enum<Type> & IAttrEnum<Type>> extends DefaultFormulaParam {
	/**
	 * 自己
	 */
	IFighter<Type> self;
	/**
	 * 目标
	 */
	IFighter<Type> target;

	public FighterAttrFormulaParam(IFighter<Type> self, IFighter<Type> target, double... values) {
		super(values);
		this.self = self;
		this.target = target;
	}

	public IFighter<Type> getSelf() {
		return self;
	}

	public IFighter<Type> getTarget() {
		return target;
	}
}
