package org.qiunet.cfg.convert;

import org.qiunet.cfg.convert.generics.LongSet;
import org.qiunet.utils.string.StringUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 所用字符串需要用 , 隔开.
 *
 * @author qiunet
 * 2020/3/11 08:26
 **/
public class LongSetCovert extends BaseObjConvert<LongSet> {
	private static final String SPLIT = ",";
	@Override
	protected LongSet fromString0(String str) {
		Long [] longs = StringUtil.conversion(str, SPLIT, Long.class);
		LongSet longSet = Stream.of(longs).collect(Collectors.toCollection(LongSet::new));
		longSet.convertToUnmodifiable();
		return longSet;
	}

	@Override
	public boolean canConvert(Class type) {
		return type == LongSet.class;
	}
}
