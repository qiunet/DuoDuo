package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaVars;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 *
 *
 * @author qiunet
 * 2020-12-30 15:59
 */
public class VarsFormulaParse<Obj extends DefaultFormulaParam> implements IFormulaParse<Obj> {
	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		formulaString = formulaString.trim();
		if (formulaString.startsWith("var")) {
			int index = Integer.parseInt(formulaString.substring(3));
			return new FormulaVars<>(index);
		}
		return null;
	}
}
