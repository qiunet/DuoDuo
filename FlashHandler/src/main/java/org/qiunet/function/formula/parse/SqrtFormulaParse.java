package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaSqrt;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

import java.util.regex.Pattern;

/***
 *
 * @author qiunet
 * 2021/12/31 15:39
 */
public class SqrtFormulaParse<Obj extends IFormulaParam> implements IFormulaParse<Obj> {

	private static final Pattern pattern = Pattern.compile("sqrt\\$\\{[0-9]+}");

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}

	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		String value = formulaString.trim();
		if (pattern.matcher(value).matches()) {
			String realValue = value.substring(value.indexOf("{") + 1, value.length() - 1);
			return new FormulaSqrt<>(context.get(Integer.parseInt(realValue)));
		}
		return null;
	}
}
