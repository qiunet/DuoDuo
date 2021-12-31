package org.qiunet.function.formula;

/***
 * Math.log10
 *
 * @author qiunet
 * 2021/12/31 17:15
 */
public class FormulaLog10<Obj extends IFormulaParam> implements IFormula<Obj>{
	private final IFormula<Obj> formula;

	public FormulaLog10(IFormula<Obj> formula) {
		this.formula = formula;
	}

	@Override
	public double cal(Obj params) {
		return Math.log10(this.formula.cal(params));
	}

	@Override
	public String toString() {
		return "log10("+formula.toString()+")";
	}
}
