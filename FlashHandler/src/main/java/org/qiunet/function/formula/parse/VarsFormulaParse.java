package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 * 自定义的一些变量.
 * 公式可以自由读取程序传入的参数. 按照参数index取值.
 *
 * @author qiunet
 * 2020-12-30 15:59
 */
public class VarsFormulaParse implements IFormulaParse {
	@Override
	public IFormula parse(FormulaParseContext  context, String formulaString) {
		formulaString = formulaString.trim();
		if (formulaString.startsWith("var")) {
			int index = Integer.parseInt(formulaString.substring(3));
			return new Formula(index);
		}
		return null;
	}

	/***
		 * 自定义的一些变量.
		 * 公式可以自由读取程序传入的参数. 按照参数index取值.
		 *
		 * 从 {@link DefaultFormulaParam#getValues()} 获取
		 *
		 * @author qiunet
		 * 2020-12-30 15:56
	 * @param index
	index
		 */
		private record Formula(int index) implements IFormula {

		@Override
			public <Obj extends DefaultFormulaParam> double cal(Obj params) {
				return params.getValues()[index];
			}

			@Override
			public String toString() {
				return "var" + index;
			}
		}
}
