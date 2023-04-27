package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.math.MathUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 16:47
 */
public class RandomFormulaParse implements IFormulaParse {
	private static final Pattern pattern = Pattern.compile("\\[(.+),(.+)\\]");
	@Override
	public IFormula parse(FormulaParseContext context, String formulaString) {
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

	/***
		 * 随机公式
		 *
		 * @author qiunet
		 * 2020-12-02 10:15
		 */
		private record Formula(IFormula left, IFormula right) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				double val1 = left.cal(params);
				double val2 = right.cal(params);
				if (val1 > val2) {
					throw new CustomException("FormulaRandom {} is error!", this.toString());
				}
				return MathUtil.random(val1, val2);
			}

			@Override
			public String toString() {
				return "[" + left.toString() + ", " + right.toString() + "]";
			}
		}
}
