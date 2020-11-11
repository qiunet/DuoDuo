package org.qiunet.cfg.convert;

import org.qiunet.utils.collection.generics.LongList;
import org.qiunet.utils.string.StringUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 所用字符串需要用 , 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class LongListCovert extends BaseObjConvert<LongList> {
	private static final String SPLIT = ",";
	@Override
	protected LongList fromString0(String str) {
		Long[] longs = StringUtil.conversion(str, SPLIT, Long.class);
		LongList longList = Stream.of(longs).collect(Collectors.toCollection(LongList::new));
		longList.convertToUnmodifiable();
		return longList;
	}

	@Override
	public boolean canConvert(Class type) {
		return type == LongList.class;
	}
}
