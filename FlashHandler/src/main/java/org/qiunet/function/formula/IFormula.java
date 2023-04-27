package org.qiunet.function.formula;

import org.qiunet.function.formula.param.DefaultFormulaParam;

/***
 * 公式接口
 * 可以扩展属性公式. 得出self 或者 target的属性.
 *
 * @author qiunet
 * 2020-12-01 18:13
 */
public interface IFormula {
	/**
	 * 计算数值.
	 * @param params 需要的参数
	 * @return 计算出来的值
	 */
	<Obj extends DefaultFormulaParam> double cal(Obj params);

	/**
	 * 不需要对象的计算.
	 * @return 计算值
	 */
	default double cal() {
		return this.cal( null);
	}
}
