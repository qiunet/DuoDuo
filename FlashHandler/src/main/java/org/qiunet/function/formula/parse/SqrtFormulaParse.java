package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.FormulaSqrt;
import org.qiunet.function.formula.IFormulaParam;


/***
 * 开方parse
 *
 * @author qiunet
 * 2021/12/31 15:39
 */
public class SqrtFormulaParse<Obj extends IFormulaParam> extends BasicBracketsFormulaParse<Obj> {

	public SqrtFormulaParse() {
		super(FormulaSqrt::new, "sqrt");
	}
}
