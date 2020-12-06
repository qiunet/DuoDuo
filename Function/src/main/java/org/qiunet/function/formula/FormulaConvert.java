package org.qiunet.function.formula;

import org.qiunet.cfg.convert.BaseObjConvert;
import org.qiunet.function.formula.parse.FormulaParseManager;

public class FormulaConvert extends BaseObjConvert<IFormula> {
	@Override
	protected IFormula fromString0(String str) {
		return FormulaParseManager.parse(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return IFormula.class.isAssignableFrom(type);
	}
}
