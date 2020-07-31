package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class IntConvert extends BaseObjConvert<Integer> {
	@Override
	protected Integer fromString0(String str) {
		return Integer.valueOf(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Integer.class || type == Integer.TYPE;
	}
}
