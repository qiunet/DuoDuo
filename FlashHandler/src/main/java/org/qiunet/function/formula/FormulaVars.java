package org.qiunet.function.formula;

import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 * 自定义的一些变量.
 * 公式可以自由读取程序传入的参数. 按照参数index取值.
 * 从 {@link DefaultFormulaParam#getValues()} 获取
 *
 * @author qiunet
 * 2020-12-30 15:56
 */
public class FormulaVars<Obj extends DefaultFormulaParam> implements IFormula<Obj> {
	/**
	 * index
	 */
	private final int index;

	public FormulaVars(int index) {
		this.index = index;
	}

	@Override
	public double cal(Obj params) {
		return params.getValues()[index];
	}

	@Override
	public String toString() {
		return "var"+index;
	}
}
