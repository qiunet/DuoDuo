package org.qiunet.function.formula.parse;

import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.enums.FighterParamSide;
import org.qiunet.function.formula.param.DefaultFormulaParam;
import org.qiunet.function.formula.param.FighterAttrFormulaParam;
import org.qiunet.utils.scanner.anno.AutoWired;

/***
 * 玩家属性公式解析
 *
 * @author qiunet
 * 2020-12-30 14:46
 */
public class FighterAttrFormulaParse implements IFormulaParse{

	@AutoWired
	private static IBasicFunction basicFunction;

	@Override
	public IFormula parse(FormulaParseContext context, String formulaString) {
		formulaString = formulaString.trim();
		for (FighterParamSide side : FighterParamSide.values()) {
			if (! formulaString.startsWith(side.name() + ".")) {
				continue;
			}

			String attrStr = formulaString.substring(side.name().length() + 1);
			Enum attr = basicFunction.parse(attrStr);
			return new Formula(side, attr);
		}

		return null;
	}

	/***
		 * 战斗玩家的属性
		 *
		 * @author qiunet
		 * 2020-12-30 14:39
		 */
		private record Formula(FighterParamSide side, Enum type) implements IFormula {


		@Override
		public String toString() {
				return side + "." + type;
			}

		@Override
		public <Obj extends DefaultFormulaParam> double cal(Obj params) {
			return side.getFighter(((FighterAttrFormulaParam) params)).getAttr(type);
		}
	}
}
