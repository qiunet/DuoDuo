package org.qiunet.function.formula;

import org.qiunet.cfg.convert.BaseObjConvert;
import org.qiunet.function.formula.parse.FormulaParseManager;

import java.lang.reflect.Field;

public class FormulaConvert extends BaseObjConvert<IFormula> {
	@Override
	public IFormula fromString(Field field, String str) {
		return FormulaParseManager.parse(str);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return IFormula.class.isAssignableFrom(type);
	}
}
