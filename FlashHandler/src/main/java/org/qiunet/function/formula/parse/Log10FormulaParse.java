package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaLog10;
import org.qiunet.function.formula.IFormulaParam;

/***
 * 对某个数值进行 log10
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class Log10FormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public Log10FormulaParse() {
		super(FormulaLog10::new, "log10");
	}
}
