package org.qiunet.cfg.convert;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2021/11/12 11:03
 */
public class FloatConvert extends BaseObjConvert<Float> {

	@Override
	public Float fromString(Field field, String str) {
		return Float.parseFloat(str);
	}

	@Override
	public boolean canConvert(Class<Float> type) {
		return type == float.class || type == Float.class;
	}
}
