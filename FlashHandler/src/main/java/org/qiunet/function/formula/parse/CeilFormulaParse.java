package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 * 往上取整
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class CeilFormulaParse extends BasicBracketsFormulaParse {

	public CeilFormulaParse() {
		super(Formula::new, "ceil");
	}

	/***
		 * Math.ceil
		 *
		 * @author qiunet
		 * 2021/12/31 17:15
		 */
		private record Formula(IFormula formula) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				return Math.ceil(this.formula.cal(params));
			}

			@Override
			public String toString() {
				return "ceil(" + formula.toString() + ")";
			}
		}

}
