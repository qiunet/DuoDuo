package org.qiunet.function.formula.parse;

import com.google.common.collect.Lists;
import org.qiunet.function.formula.IFormula;

import java.util.List;

/***
 * 公式解析上下文
 *
 * @author qiunet
 * 2020-12-02 10:58
 */
class FormulaParseContext {
	private final List<IFormula> brackets = Lists.newArrayListWithCapacity(2);

	FormulaParseContext(){}

	/**
	 * 加入括号预处理列表
	 * @param formula
	 * @return
	 */
	int add(IFormula formula) {
		brackets.add(formula);
		return brackets.size() - 1;
	}

	IFormula get(int index) {
		return brackets.get(index);
	}
}
