package org.qiunet.function.formula.parse;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.function.formula.IFormula;
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
			return new Formula<>(side, attr);
		}

		return null;
	}

	/***
	 * 战斗玩家的属性
	 *
	 * @author qiunet
	 * 2020-12-30 14:39
	 */
	private static class Formula<Type extends Enum<Type> & IAttrEnum<Type>> implements IFormula<FighterAttrFormulaParam<Type>> {
		private final FighterParamSide side;
		private final Type type;

		public Formula(FighterParamSide side, Type type) {
			this.side = side;
			this.type = type;
		}

		@Override
		public double cal(FighterAttrFormulaParam<Type> params) {
			return side.getFighter(params).getAttr(type);
		}

		@Override
		public String toString() {
			return side + "." + type;
		}
	}
}
