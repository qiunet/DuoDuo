package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;


/***
 * 开方parse
 *
 * @author qiunet
 * 2021/12/31 15:39
 */
public class SqrtFormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public SqrtFormulaParse() {
		super(Formula::new, "sqrt");
	}

	/***
	 * 开方
	 *
	 * @author qiunet
	 * 2021/12/31 15:41
	 */
	private static class Formula<Obj extends IFormulaParam> implements IFormula<Obj> {
		private final IFormula<Obj> formula;

		public Formula(IFormula<Obj> formula) {
			this.formula = formula;
		}

		@Override
		public double cal(Obj params) {
			return Math.sqrt(formula.cal(params));
		}

		@Override
		public String toString() {
			return "sqrt("+formula.toString()+")";
		}
	}

}
