package org.qiunet.function.formula;

/***
 * 开方
 *
 * @author qiunet
 * 2021/12/31 15:41
 */
public class FormulaSqrt<Obj extends IFormulaParam> implements IFormula<Obj>{
	private final IFormula<Obj> formula;

	public FormulaSqrt(IFormula<Obj> formula) {
		this.formula = formula;
	}

	@Override
	public double cal(Obj params) {
		return Math.sqrt(formula.cal(params));
	}

	@Override
	public String toString() {
		return "sqrt("+formula.toString()+")";
	}
}
