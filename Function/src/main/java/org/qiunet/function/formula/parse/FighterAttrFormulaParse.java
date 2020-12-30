package org.qiunet.function.formula.parse;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.base.IBasicFunction;
import org.qiunet.function.formula.FormulaFighterAttr;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;
import org.qiunet.function.formula.enums.FighterParamSide;
import org.qiunet.function.formula.param.FighterAttrFormulaParam;
import org.qiunet.utils.scanner.anno.AutoWired;

/***
 * 玩家属性公式解析
 *
 * @author qiunet
 * 2020-12-30 14:46
 */
public class FighterAttrFormulaParse <Type extends Enum<Type> & IAttrEnum<Type>>
		implements IFormulaParse<FighterAttrFormulaParam<Type>>{

	@AutoWired
	private static IBasicFunction basicFunction;

	@Override
	public IFormula<FighterAttrFormulaParam<Type>> parse(
		FormulaParseContext<FighterAttrFormulaParam<Type>> context,
		 String formulaString) {
		formulaString = formulaString.trim();
		for (FighterParamSide side : FighterParamSide.values()) {
			if (! formulaString.startsWith(side.name() + ".")) {
				continue;
			}

			String attrStr = formulaString.substring(side.name().length() + 1);
			Type attr = basicFunction.parse(attrStr);
			return new FormulaFighterAttr<>(side, attr);
		}

		return null;
	}
}
