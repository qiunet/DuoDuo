package org.qiunet.cfg.convert;

import org.qiunet.utils.collection.generics.LongSet;
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
public class LongSetCovert extends BaseObjConvert<LongSet> {
	private static final String SPLIT = ";";
	@Override
	public LongSet fromString(Field field, String str) {
		Long [] longs = StringUtil.conversion(str, SPLIT, Long.class);
		LongSet longSet = Stream.of(longs).collect(Collectors.toCollection(LongSet::new));
		longSet.convertToUnmodifiable();
		return longSet;
	}
}
