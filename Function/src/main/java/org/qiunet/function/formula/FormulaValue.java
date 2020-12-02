package org.qiunet.function.formula;

/***
 * 固定值.
 *
 * @author qiunet
 * 2020-12-01 18:28
 */
public class FormulaValue<Obj> implements IFormula<Obj> {
	private double value;

	public FormulaValue(double value) {
		this.value = value;
	}

	@Override
	public double cal(Obj self, Obj target, double... vars) {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
