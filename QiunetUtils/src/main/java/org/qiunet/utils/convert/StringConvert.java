package org.qiunet.utils.convert;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class StringConvert extends BaseObjConvert<String> {
	@Override
	public String fromString(Field field, String str) {
		return str;
	}
}
