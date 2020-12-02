package org.qiunet.function.formula;

/***
 * 表达式
 *
 * @author qiunet
 * 2020-12-01 18:29
 */
public class FormulaExpression<Obj extends IFormulaParam> implements IFormula<Obj> {
	private IFormula<Obj> left;
	private IFormula<Obj> right;
	private Sign sign;

	public FormulaExpression(IFormula<Obj> left, IFormula<Obj> right, Sign sign) {
		this.left = left;
		this.right = right;
		this.sign = sign;
	}

	@Override
	public double cal(Obj params) {
		return sign.cal(left.cal(params), right.cal(params));
	}

	@Override
	public String toString() {
		return left.toString() + " " + sign.getSymbol() +" "+ right.toString();
	}
}
