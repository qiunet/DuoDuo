package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaExpression;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.Sign;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:33
 */
public class ExpressionFormulaParse<Obj> implements IFormulaParse<Obj> {
	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		for (Sign sign : Sign.values) {
			int index = formulaString.indexOf(sign.getSymbol());
			if (index == -1) {
				continue;
			}

			String left = formulaString.substring(0, index);
			String right = formulaString.substring(index + 1);
			return new FormulaExpression<>(
				this._Parse(context, left),
				this._Parse(context, right), sign);
		}
		return null;
	}


	@Override
	public int order() {
		return Integer.MAX_VALUE - 10;
	}
}
