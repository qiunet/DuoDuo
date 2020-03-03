package org.qiunet.cfg.convert;

import org.qiunet.utils.string.StringUtil;

/***
 *
 *
 * @author qiunet
 * 2020-03-03 17:46
 ***/
public class LongArrayConvert extends BaseObjConvert<long[]> {
	private static final long [] EMPTY_ARRAY = new long[0];
	@Override
	protected long[] fromString0(String str) {
		if (StringUtil.isEmpty(str)) {
			return EMPTY_ARRAY;
		}
		String[] splits = StringUtil.split(str, ",");
		long [] retArray = new long[splits.length];
		for (int i = 0; i < splits.length; i++) {
			retArray[i] = Long.parseLong(splits[i]);
		}
		return retArray;
	}

	@Override
	public boolean canConvert(Class aClass) {
		return aClass == long[].class;
	}
}
