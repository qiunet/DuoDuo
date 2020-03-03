package org.qiunet.cfg.convert;

import org.qiunet.utils.string.StringUtil;

/***
 *
 *
 * @author qiunet
 * 2020-03-03 17:46
 ***/
public class IntArrayConvert extends BaseObjConvert<int[]> {
	private static final int [] EMPTY_ARRAY = new int[0];
	@Override
	protected int[] fromString0(String str) {
		if (StringUtil.isEmpty(str)) {
			return EMPTY_ARRAY;
		}
		String[] splits = StringUtil.split(str, ",");
		int [] retArray = new int[splits.length];
		for (int i = 0; i < splits.length; i++) {
			retArray[i] = Integer.parseInt(splits[i]);
		}
		return retArray;
	}

	@Override
	public boolean canConvert(Class aClass) {
		return aClass == int[].class;
	}
}
