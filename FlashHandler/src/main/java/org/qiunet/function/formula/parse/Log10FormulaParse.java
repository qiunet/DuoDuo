package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

/***
 * 对某个数值进行 log10
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class Log10FormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public Log10FormulaParse() {
		super(Formula::new, "log10");
	}

	/***
	 * Math.log10
	 *
	 * @author qiunet
	 * 2021/12/31 17:15
	 */
	private static class Formula<Obj extends IFormulaParam> implements IFormula<Obj> {
		private final IFormula<Obj> formula;

		public Formula(IFormula<Obj> formula) {
			this.formula = formula;
		}

		@Override
		public double cal(Obj params) {
			return Math.log10(this.formula.cal(params));
		}

		@Override
		public String toString() {
			return "log10("+formula.toString()+")";
		}
	}

}
