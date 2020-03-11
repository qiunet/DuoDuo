package org.qiunet.cfg.convert;

import org.qiunet.cfg.convert.generics.StringSet;
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
public class StringSetCovert extends BaseObjConvert<StringSet> {
	private static final String SPLIT = ",";
	@Override
	protected StringSet fromString0(String str) {
		String[] strings = StringUtil.split(str, SPLIT);
		List<String> collect = Stream.of(strings).collect(Collectors.toList());
		StringSet stringSet = new StringSet(collect);
		stringSet.safeLock();
		return stringSet;
	}

	@Override
	public boolean canConvert(Class type) {
		return type == StringSet.class;
	}
}
