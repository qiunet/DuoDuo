package org.qiunet.cfg.convert;

import org.qiunet.utils.collection.generics.IntegerList;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 所用字符串需要用 , 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class IntegerListCovert extends BaseObjConvert<IntegerList> {
	private static final String SPLIT = ";";
	@Override
	public IntegerList fromString(Field field, String str) {
		Integer[] integers = StringUtil.conversion(str, SPLIT, Integer.class);
		IntegerList integerList = Stream.of(integers).collect(Collectors.toCollection(IntegerList::new));
		integerList.convertToUnmodifiable();
		return integerList;
	}
}
