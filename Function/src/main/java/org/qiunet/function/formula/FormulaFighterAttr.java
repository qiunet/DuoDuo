package org.qiunet.function.formula;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.formula.enums.FighterParamSide;
import org.qiunet.function.formula.param.FighterAttrFormulaParam;

/***
 * 战斗玩家的属性
 *
 * @author qiunet
 * 2020-12-30 14:39
 */
public class FormulaFighterAttr<Type extends Enum<Type> & IAttrEnum<Type>> implements IFormula<FighterAttrFormulaParam<Type>> {
	private FighterParamSide side;
	private Type type;

	public FormulaFighterAttr(FighterParamSide side, Type type) {
		this.side = side;
		this.type = type;
	}

	@Override
	public double cal(FighterAttrFormulaParam<Type> params) {
		return side.getFighter(params).getAttr(type);
	}
}
