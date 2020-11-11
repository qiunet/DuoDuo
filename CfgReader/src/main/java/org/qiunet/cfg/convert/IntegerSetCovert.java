package org.qiunet.cfg.convert;

import org.qiunet.utils.collection.generics.IntegerSet;
import org.qiunet.utils.string.StringUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 所用字符串需要用 , 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class IntegerSetCovert extends BaseObjConvert<IntegerSet> {
	private static final String SPLIT = ",";
	@Override
	protected IntegerSet fromString0(String str) {
		Integer[] integers = StringUtil.conversion(str, SPLIT, Integer.class);
		IntegerSet integerSet = Stream.of(integers).collect(Collectors.toCollection(IntegerSet::new));
		integerSet.convertToUnmodifiable();
		return integerSet;
	}

	@Override
	public boolean canConvert(Class type) {
		return type == IntegerSet.class;
	}
}
