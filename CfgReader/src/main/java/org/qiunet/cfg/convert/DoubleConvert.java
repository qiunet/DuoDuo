package org.qiunet.cfg.convert;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 14:57
 **/
public class DoubleConvert extends BaseObjConvert<Double> {
	@Override
	public Double fromString(Field field, String str) {
		return Double.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<Double> type) {
		return type == Double.TYPE || type == Double.class;
	}
}
