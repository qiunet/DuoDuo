package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class StringConvert extends BaseObjConvert<String> {
	@Override
	protected String fromString0(String str) {
		return str;
	}

	@Override
	public boolean canConvert(Class type) {
		return type == String.class;
	}
}
