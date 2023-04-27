package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 * 对某个数值进行 log
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class LogFormulaParse extends BasicBracketsFormulaParse {

	public LogFormulaParse() {
		super(Formula::new, "log");
	}

	/***
		 * Math.log
		 *
		 * @author qiunet
		 * 2021/12/31 17:15
		 */
		private record Formula(IFormula formula) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				return Math.log(this.formula.cal(params));
			}

			@Override
			public String toString() {
				return "log(" + formula.toString() + ")";
			}
		}

}
