package org.qiunet.function.formula;

import com.google.common.base.Preconditions;

/***
 * 外界参数. 传入index使用
 *
 * @author qiunet
 * 2020-12-01 18:26
 */
public class FormulaVar<Obj> implements IFormula<Obj> {
	private int index;

	public FormulaVar(int index) {
		Preconditions.checkState(index >= 0);
		this.index = index;
	}

	@Override
	public double cal(Obj self, Obj target, double... vars) {
		return vars[index];
	}

	@Override
	public String toString() {
		return "var"+index;
	}
}
