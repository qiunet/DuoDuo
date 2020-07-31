package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class ShortConvert extends BaseObjConvert<Short> {
	@Override
	protected Short fromString0(String str) {
		return Short.valueOf(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Short.class || type == Short.TYPE;
	}
}
