package org.qiunet.function.formula.parse;

import com.google.common.collect.Lists;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;

import java.util.List;

/***
 * 公式解析上下文
 *
 * @author qiunet
 * 2020-12-02 10:58
 */
class FormulaParseContext<Obj extends IFormulaParam> {
	private final List<IFormula<Obj>> brackets = Lists.newArrayListWithCapacity(2);

	FormulaParseContext(){}

	/**
	 * 加入括号预处理列表
	 * @param formula 公式
	 * @return
	 */
	int add(IFormula<Obj> formula) {
		brackets.add(formula);
		return brackets.size() - 1;
	}

	IFormula<Obj> get(int index) {
		return brackets.get(index);
	}
}
