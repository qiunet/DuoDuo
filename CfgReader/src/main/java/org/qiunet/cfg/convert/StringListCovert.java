package org.qiunet.cfg.convert;

import org.qiunet.utils.collection.generics.StringList;
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
public class StringListCovert extends BaseObjConvert<StringList> {
	private static final String SPLIT = ",";
	@Override
	public StringList fromString(Field field, String str) {
		String[] strings = StringUtil.split(str, SPLIT);
		StringList stringList = Stream.of(strings).collect(Collectors.toCollection(StringList::new));
		stringList.convertToUnmodifiable();
		return stringList;
	}
}
