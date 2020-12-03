package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaValue;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:03
 */
public class ValueFormulaParse<Obj extends IFormulaParam> implements IFormulaParse<Obj> {

	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		String value = formulaString.trim();
		if (value.matches("[0-9]+%{0,2}")){
			int length = value.length();
			double val = 0;
			if (value.endsWith("%%")) {
				val = Double.parseDouble(value.substring(0, length - 2)) / 10000;
			}else if (value.endsWith("%")) {
				val = Double.parseDouble(value.substring(0, length - 1)) / 100;
			}else {
				val = Double.parseDouble(value);
			}
			return new FormulaValue<>(val, value);
		}
		return null;
	}
}
