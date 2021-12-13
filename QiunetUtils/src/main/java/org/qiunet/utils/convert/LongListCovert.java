package org.qiunet.utils.convert;

import org.qiunet.utils.collection.generics.LongList;
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
public class LongListCovert extends BaseObjConvert<LongList> {
	private static final String SPLIT = ";";
	@Override
	public LongList fromString(Field field, String str) {
		Long[] longs = StringUtil.conversion(str, SPLIT, Long.class);
		LongList longList = Stream.of(longs).collect(Collectors.toCollection(LongList::new));
		longList.convertToUnmodifiable();
		return longList;
	}
}
