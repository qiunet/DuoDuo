package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:10
 */
public class FormulaParseManager {
	/**
	 * 解析公式
	 * @param formulaString
	 * @param <Obj>
	 * @return
	 */
	public static <Obj>IFormula<Obj> parse(String formulaString) {
		return FormulaParseManager0.instance.parse(formulaString);
	}
}
