package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 * 往下取整
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class FloorFormulaParse extends BasicBracketsFormulaParse {

	public FloorFormulaParse() {
		super(Formula::new, "floor");
	}

	/***
		 * Math.floor
		 *
		 * @author qiunet
		 * 2021/12/31 17:15
		 */
		private record Formula(IFormula formula) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				return Math.floor(this.formula.cal(params));
			}

			@Override
			public String toString() {
				return "floor(" + formula.toString() + ")";
			}
		}

}
