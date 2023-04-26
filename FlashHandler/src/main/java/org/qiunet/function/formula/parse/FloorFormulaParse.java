package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

/***
 * 往下取整
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class FloorFormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public FloorFormulaParse() {
		super(Formula::new, "floor");
	}

	/***
	 * Math.floor
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
			return Math.floor(this.formula.cal(params));
		}

		@Override
		public String toString() {
			return "floor("+formula.toString()+")";
		}
	}

}
