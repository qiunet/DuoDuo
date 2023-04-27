package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.Sign;
import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:33
 */
public class ExpressionFormulaParse implements IFormulaParse {
	@Override
	public IFormula parse(FormulaParseContext context, String formulaString) {
		for (Sign sign : Sign.values) {
			int index = formulaString.indexOf(sign.getSymbol());
			if (index == -1) {
				continue;
			}

			String left = formulaString.substring(0, index);
			String right = formulaString.substring(index + 1);
			return new Formula(
				this._Parse(context, left),
				this._Parse(context, right), sign);
		}
		return null;
	}


	@Override
	public int order() {
		return Integer.MAX_VALUE - 10;
	}

	/***
		 * 表达式
		 *
		 * @author qiunet
		 * 2020-12-01 18:29
		 */
		private record Formula(IFormula left, IFormula right,
														  Sign sign) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				return sign.cal(left.cal(params), right.cal(params));
			}

			@Override
			public String toString() {
				return left.toString() + " " + sign.getSymbol() + " " + right.toString();
			}
		}
}
