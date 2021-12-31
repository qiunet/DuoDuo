package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaLog;
import org.qiunet.function.formula.IFormulaParam;

/***
 * 对某个数值进行 log
 *
 * @author qiunet
 * 2021/12/31 17:13
 */
public class LogFormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public LogFormulaParse() {
		super(FormulaLog::new, "log");
	}
}
