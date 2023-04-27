package org.qiunet.function.formula.parse;

import org.qiunet.function.formula.IFormula;


/***
 *
 * @author qiunet
 * 2021/12/31 17:07
 */
@FunctionalInterface
public interface IFormulaSupplier {

	IFormula get(IFormula formula);
}
