package org.qiunet.utils.convert;

import java.lang.reflect.Field;
import java.util.List;

/***
 * 所用字符串需要用 ; 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class ListIntegerCovert extends BaseObjListConvert<Integer> {
	private static final IntegerListCovert covert = new IntegerListCovert();
	@Override
	public List<Integer> fromString(Field field, String str) {
		return covert.fromString(field, str);
	}
}
