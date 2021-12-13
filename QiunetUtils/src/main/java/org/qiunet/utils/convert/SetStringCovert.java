package org.qiunet.utils.convert;

import java.lang.reflect.Field;
import java.util.Set;

/***
 * 所用字符串需要用 , 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class SetStringCovert extends BaseObjSetConvert<String> {
	private static final StringSetCovert covert = new StringSetCovert();
	@Override
	public Set<String> fromString(Field field, String str) {
		return covert.fromString(field, str);
	}
}
