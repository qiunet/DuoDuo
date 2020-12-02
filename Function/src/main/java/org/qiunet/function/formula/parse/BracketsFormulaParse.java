package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaBrackets;
import org.qiunet.function.formula.IFormula;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:03
 */
public class BracketsFormulaParse<Obj> implements IFormulaParse<Obj> {

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}

	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		String value = formulaString.trim();
		if (value.matches("\\$\\{[0-9]+}")) {
			String realValue = value.replace("${", "").replace("}", "");
			return new FormulaBrackets<>(context.get(Integer.parseInt(realValue)));
		}
		return null;
	}
}
