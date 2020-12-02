package org.qiunet.function.formula;

/***
 * 括号
 *
 * @author qiunet
 * 2020-12-02 10:24
 */
public class FormulaBrackets<Obj extends IFormulaParam> implements IFormula<Obj> {
	private IFormula<Obj> formula;

	public FormulaBrackets(IFormula<Obj> formula) {
		this.formula = formula;
	}

	@Override
	public double cal(Obj params) {
		return formula.cal(params);
	}

	@Override
	public String toString() {
		return "("+formula.toString()+")";
	}
}
