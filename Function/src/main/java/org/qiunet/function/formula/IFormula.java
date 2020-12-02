package org.qiunet.function.formula;

/***
 * 公式接口
 * 可以扩展属性公式. 得出self 或者 target的属性.
 *
 * @author qiunet
 * 2020-12-01 18:13
 */
public interface IFormula<Obj extends IFormulaParam> {
	/**
	 * 计算数值.
	 * @param params 需要的参数
	 * @return
	 */
	double cal(Obj params);

	/**
	 * 不需要对象的计算.
	 * @return
	 */
	default double cal() {
		return this.cal( null);
	}
}
