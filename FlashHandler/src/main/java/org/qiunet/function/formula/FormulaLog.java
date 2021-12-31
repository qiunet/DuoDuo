package org.qiunet.function.formula;

/***
 * Math.log
 *
 * @author qiunet
 * 2021/12/31 17:15
 */
public class FormulaLog<Obj extends IFormulaParam> implements IFormula<Obj>{
	private final IFormula<Obj> formula;

	public FormulaLog(IFormula<Obj> formula) {
		this.formula = formula;
	}

	@Override
	public double cal(Obj params) {
		return Math.log(this.formula.cal(params));
	}

	@Override
	public String toString() {
		return "log("+formula.toString()+")";
	}
}
