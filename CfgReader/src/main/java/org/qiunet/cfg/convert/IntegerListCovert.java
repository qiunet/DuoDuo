package org.qiunet.cfg.convert;

import org.qiunet.cfg.convert.generics.IntegerList;
import org.qiunet.utils.string.StringUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 所用字符串需要用 , 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class IntegerListCovert extends BaseObjConvert<IntegerList> {
	private static final String SPLIT = ",";
	@Override
	protected IntegerList fromString0(String str) {
		Integer[] integers = StringUtil.conversion(str, SPLIT, Integer.class);
		List<Integer> collect = Stream.of(integers).collect(Collectors.toList());
		IntegerList integerList = new IntegerList(collect);
		integerList.convertSafe();
		return integerList;
	}

	@Override
	public boolean canConvert(Class type) {
		return type == IntegerList.class;
	}
}
