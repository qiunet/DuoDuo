package org.qiunet.function.formula;

import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.math.MathUtil;

/***
 * 随机公式
 *
 * @author qiunet
 * 2020-12-02 10:15
 */
public class FormulaRandom<Obj> implements IFormula<Obj> {
	private IFormula<Obj> left;
	private IFormula<Obj> right;

	public FormulaRandom(IFormula<Obj> left, IFormula<Obj> right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public double cal(Obj self, Obj target, double... vars) {
		double val1 = left.cal(self, target, vars);
		double val2 = right.cal(self, target, vars);
		if (val1 > val2) {
			throw new CustomException("FormulaRandom {} is error!", this.toString());
		}
		return MathUtil.random(val1, val2);
	}

	@Override
	public String toString() {
		return "["+ left.toString() +"," + right.toString()+"]";
	}
}
