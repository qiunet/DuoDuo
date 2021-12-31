package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaBrackets;
import org.qiunet.function.formula.IFormulaParam;

/***
 *
 * 括号的parse
 *
 * @author qiunet
 * 2020-12-02 11:03
 */
public class BracketsFormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {
	public BracketsFormulaParse() {
		super(FormulaBrackets::new, "");
	}
}
