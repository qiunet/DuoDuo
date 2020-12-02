package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaRandom;
import org.qiunet.function.formula.IFormula;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 16:47
 */
public class RandomFormulaParse<Obj> implements IFormulaParse<Obj> {
	private static final Pattern pattern = Pattern.compile("\\[(.+),(.+)\\]");
	@Override
	public IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
		formulaString = formulaString.trim();
		Matcher matcher = pattern.matcher(formulaString);
		if (matcher.find() && matcher.groupCount() == 2) {
			String left = matcher.group(1);
			String right = matcher.group(2);
			return new FormulaRandom<>(
				this._Parse(context, left),
				this._Parse(context, right)
				);
		}
		return null;
	}
}
