package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class BooleanConvert extends BaseObjConvert<Boolean> {
	@Override
	protected Boolean fromString0(String str) {
		return "true".equals(str) || "1".equals(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Boolean.class || type == Boolean.TYPE;
	}
}
