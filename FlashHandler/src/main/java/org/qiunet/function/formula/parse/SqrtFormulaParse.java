package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;


/***
 * 开方parse
 *
 * @author qiunet
 * 2021/12/31 15:39
 */
public class SqrtFormulaParse extends BasicBracketsFormulaParse {

	public SqrtFormulaParse() {
		super(Formula::new, "sqrt");
	}

	/***
		 * 开方
		 *
		 * @author qiunet
		 * 2021/12/31 15:41
		 */
		private record Formula(IFormula formula) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				return Math.sqrt(formula.cal(params));
			}

			@Override
			public String toString() {
				return "sqrt(" + formula.toString() + ")";
			}
		}

}
