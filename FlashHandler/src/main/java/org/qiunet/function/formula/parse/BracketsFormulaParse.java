package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 *
 * 括号的parse
 *
 * @author qiunet
 * 2020-12-02 11:03
 */
public class BracketsFormulaParse extends BasicBracketsFormulaParse {
	public BracketsFormulaParse() {
		super(Formula::new, "");
	}

	/***
		 * 括号
		 *
		 * @author qiunet
		 * 2020-12-02 10:24
		 */
		private record Formula(IFormula formula) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				return formula.cal(params);
			}

			@Override
			public String toString() {
				return "(" + formula.toString() + ")";
			}
		}

}
