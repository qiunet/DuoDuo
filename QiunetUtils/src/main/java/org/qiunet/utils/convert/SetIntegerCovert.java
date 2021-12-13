package org.qiunet.utils.convert;

import java.lang.reflect.Field;
import java.util.Set;

/***
 * 所用字符串需要用 ; 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class SetIntegerCovert extends BaseObjSetConvert<Integer> {
	private static final IntegerSetCovert covert = new IntegerSetCovert();
	@Override
	public Set<Integer> fromString(Field field, String str) {
		return covert.fromString(field, str);
	}
}
