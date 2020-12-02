package org.qiunet.function.formula;

/***
 * 公式接口
 * 可以扩展属性公式. 得出self 或者 target的属性.
 *
 * @author qiunet
 * 2020-12-01 18:13
 */
public interface IFormula<Obj> {
	/**
	 * 计算数值.
	 * @param self 需要的对象 self
	 * @param target 需要的对象 target
	 * @param vars 其它参数
	 * @return
	 */
	double cal(Obj self, Obj target, double ... vars);
}
