package org.qiunet.function.formula;

/***
 * 固定值.
 *
 * @author qiunet
 * 2020-12-01 18:28
 */
public class FormulaValue<Obj extends IFormulaParam> implements IFormula<Obj> {
	private final String strVal;
	private final double value;
	public FormulaValue(double value, String strVal) {
		this.strVal = strVal;
		this.value = value;
	}

	@Override
	public double cal(Obj params) {
		return value;
	}

	@Override
	public String toString() {
		return strVal;
	}
}
