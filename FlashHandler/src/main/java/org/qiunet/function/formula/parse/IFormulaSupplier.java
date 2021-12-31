package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;


/***
 *
 * @author qiunet
 * 2021/12/31 17:07
 */
@FunctionalInterface
public interface IFormulaSupplier<Obj extends IFormulaParam> {

	IFormula<Obj> get(IFormula<Obj> formula);
}
