package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;

import java.util.regex.Pattern;

/***
 * 带括号的基础parse
 *
 * @author qiunet
 * 2021/12/31 17:02
 */
public abstract class BasicBracketsFormulaParse implements IFormulaParse {
	private final Pattern pattern;
	private final IFormulaSupplier supplier;

	public BasicBracketsFormulaParse(IFormulaSupplier supplier, String name) {
		this.pattern = Pattern.compile(name+"\\$\\{[0-9]+}");
		this.supplier = supplier;
	}

	@Override
	public IFormula parse(FormulaParseContext context, String formulaString) {
		String value = formulaString.trim();
		if (pattern.matcher(value).matches()) {
			String realValue = value.substring(value.indexOf("{") + 1, value.length() - 1);
			return supplier.get(context.get(Integer.parseInt(realValue)));
		}
		return null;
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}
}
