package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 10:26
 */
public interface IFormulaParse {
	/**
	 * 解析公式.
	 * @param context 解析上下文
	 * @return null 表示没有匹配上
	 */
	IFormula parse(FormulaParseContext context, String formulaString);
	/**
	 * 括号先执行.
	 * 然后表达式
	 * 其它平级
	 * 有优先级. 需要调大order
	 * @return
	 */
	default int order(){
		return 0;
	}

	default IFormula _Parse(FormulaParseContext context, String formulaString) {
		return FormulaParseManager.FormulaParseManager0.instance.parse(context, formulaString);
	}
}
