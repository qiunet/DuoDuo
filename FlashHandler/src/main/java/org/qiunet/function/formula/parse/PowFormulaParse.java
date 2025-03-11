package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * pow处理
 */
public class PowFormulaParse implements IFormulaParse {
	private static final Pattern pattern = Pattern.compile("pow\\[(.+),(.+)\\]");
	@Override
	public IFormula parse(FormulaParseContext  context, String formulaString) {
		formulaString = formulaString.trim();
		Matcher matcher = pattern.matcher(formulaString);
		if (matcher.find() && matcher.groupCount() == 2) {
			String left = matcher.group(1);
			String right = matcher.group(2);
			return new Formula(
				this._Parse(context, left),
				this._Parse(context, right)
				);
		}
		return null;
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}

	/***
	 * pow公式
	 */
	private static class Formula implements IFormula  {
		private final IFormula  left;
		private final IFormula  right;

		public Formula(IFormula  left, IFormula  right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public <Obj extends DefaultFormulaParam> double cal(Obj params) {
			double val1 = left.cal(params);
			double val2 = right.cal(params);
			return Math.pow(val1, val2);
		}

		@Override
		public String toString() {
			return "pow["+ left.toString() +", " + right.toString()+"]";
		}
	}
}
