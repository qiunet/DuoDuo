package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

import java.util.regex.Pattern;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:03
 */
public class ValueFormulaParse implements IFormulaParse {
	private static final Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
	@Override
	public int order() {
		return Integer.MAX_VALUE - 1;
	}

	@Override
	public IFormula parse(FormulaParseContext context, String formulaString) {
		String value = formulaString.trim();
		if (pattern.matcher(value).matches()){
			return new Formula(value);
		}
		return null;
	}

	/***
	 * 固定值.
	 *
	 * @author qiunet
	 * 2020-12-01 18:28
	 */
	private static class Formula implements IFormula {
		private final String strVal;
		private final double value;
		public Formula(String strVal) {
			this.strVal = strVal;
			this.value = Double.parseDouble(strVal);
		}

		@Override
		public <Obj extends DefaultFormulaParam> double cal(Obj params) {
			return value;
		}

		@Override
		public String toString() {
			return strVal;
		}
	}

}
