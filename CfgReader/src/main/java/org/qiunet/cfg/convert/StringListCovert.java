package org.qiunet.cfg.convert;

import org.qiunet.cfg.convert.generics.StringList;
import org.qiunet.utils.string.StringUtil;

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
	protected StringList fromString0(String str) {
		String[] strings = StringUtil.split(str, SPLIT);
		StringList stringList = Stream.of(strings).collect(Collectors.toCollection(StringList::new));
		stringList.convertToUnmodifiable();
		return stringList;
	}

	@Override
	public boolean canConvert(Class type) {
		return type == StringList.class;
	}
}
