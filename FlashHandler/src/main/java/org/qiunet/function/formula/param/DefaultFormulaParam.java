package org.qiunet.function.formula.param;

import org.qiunet.function.formula.IFormulaParam;

/***
 * 默认的参数.
 *
 * @author qiunet
 * 2020-12-30 12:43
 */
public class DefaultFormulaParam implements IFormulaParam {
	private static final DefaultFormulaParam DEFAULT_PARAMS = new DefaultFormulaParam();
	/**
	 * 参数.
	 */
	private final double[] values;
	protected DefaultFormulaParam(double ... values) {
		this.values = values;
	}

	public static DefaultFormulaParam valueOf(double ... values) {
		if (values.length == 0) return DEFAULT_PARAMS;
		return new DefaultFormulaParam(values);
	}

	public double[] getValues() {
		return values;
	}
}
