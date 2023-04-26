package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

/***
 * 对某个数值进行 log
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class LogFormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public LogFormulaParse() {
		super(Formula::new, "log");
	}

	/***
	 * Math.log
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
			return Math.log(this.formula.cal(params));
		}

		@Override
		public String toString() {
			return "log("+formula.toString()+")";
		}
	}

}
