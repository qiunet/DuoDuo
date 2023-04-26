package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

/***
 * 往上取整
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class CeilFormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public CeilFormulaParse() {
		super(Formula::new, "ceil");
	}

	/***
	 * Math.ceil
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
			return Math.ceil(this.formula.cal(params));
		}

		@Override
		public String toString() {
			return "ceil("+formula.toString()+")";
		}
	}

}
