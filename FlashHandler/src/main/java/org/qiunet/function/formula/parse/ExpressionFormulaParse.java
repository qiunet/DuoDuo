package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;
import org.qiunet.function.formula.Sign;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:33
 */
public class ExpressionFormulaParse<Obj extends IFormulaParam> implements IFormulaParse<Obj> {
	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		for (Sign sign : Sign.values) {
			int index = formulaString.indexOf(sign.getSymbol());
			if (index == -1) {
				continue;
			}

			String left = formulaString.substring(0, index);
			String right = formulaString.substring(index + 1);
			return new Formula<>(
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
	private static class Formula<Obj extends IFormulaParam> implements IFormula<Obj> {
		private final IFormula<Obj> left;
		private final IFormula<Obj> right;
		private final Sign sign;

		public Formula(IFormula<Obj> left, IFormula<Obj> right, Sign sign) {
			this.left = left;
			this.right = right;
			this.sign = sign;
		}

		@Override
		public double cal(Obj params) {
			return sign.cal(left.cal(params), right.cal(params));
		}

		@Override
		public String toString() {
			return left.toString() + " " + sign.getSymbol() +" "+ right.toString();
		}
	}
}
