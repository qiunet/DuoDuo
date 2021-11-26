package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaBrackets;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

import java.util.regex.Pattern;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:03
 */
public class BracketsFormulaParse<Obj extends IFormulaParam> implements IFormulaParse<Obj> {
	private static final Pattern pattern = Pattern.compile("\\$\\{[0-9]+}");

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}

	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		String value = formulaString.trim();
		if (pattern.matcher(value).matches()) {
			String realValue = value.substring(2, value.length() - 1);
			return new FormulaBrackets<>(context.get(Integer.parseInt(realValue)));
		}
		return null;
	}
}
