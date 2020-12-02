package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaValue;
import org.qiunet.function.formula.IFormula;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:03
 */
public class ValueFormulaParse<Obj> implements IFormulaParse<Obj> {

	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		String value = formulaString.trim();
		if (value.matches("[0-9]+")){
			return new FormulaValue<>(Double.parseDouble(value));
		}
		return null;
	}
}
