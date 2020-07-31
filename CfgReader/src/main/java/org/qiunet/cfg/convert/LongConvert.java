package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class LongConvert extends BaseObjConvert<Long> {
	@Override
	protected Long fromString0(String str) {
		return Long.valueOf(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Long.class || type == Long.TYPE;
	}
}
