package org.qiunet.cfg.convert;

import java.lang.reflect.Field;
import java.util.List;

/***
 * 所用字符串需要用 , 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class ListStringCovert extends BaseObjListConvert<String> {
	private static final StringListCovert convert = new StringListCovert();
	@Override
	public List<String> fromString(Field field, String str) {
		return convert.fromString(field, str);
	}
}
