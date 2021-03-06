package org.qiunet.function.formula;

import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.math.MathUtil;

/***
 * 随机公式
 *
 * @author qiunet
 * 2020-12-02 10:15
 */
public class FormulaRandom<Obj extends IFormulaParam> implements IFormula<Obj> {
	private final IFormula<Obj> left;
	private final IFormula<Obj> right;

	public FormulaRandom(IFormula<Obj> left, IFormula<Obj> right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public double cal(Obj params) {
		double val1 = left.cal(params);
		double val2 = right.cal(params);
		if (val1 > val2) {
			throw new CustomException("FormulaRandom {} is error!", this.toString());
		}
		return MathUtil.random(val1, val2);
	}

	@Override
	public String toString() {
		return "["+ left.toString() +", " + right.toString()+"]";
	}
}
