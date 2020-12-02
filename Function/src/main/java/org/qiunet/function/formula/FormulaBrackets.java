package org.qiunet.function.formula;

/***
 * 括号
 *
 * @author qiunet
 * 2020-12-02 10:24
 */
public class FormulaBrackets<Obj> implements IFormula<Obj> {
	private IFormula<Obj> formula;

	public FormulaBrackets(IFormula<Obj> formula) {
		this.formula = formula;
	}

	@Override
	public double cal(Obj self, Obj target, double... vars) {
		return formula.cal(self, target, vars);
	}

	@Override
	public String toString() {
		return "("+formula.toString()+")";
	}
}
