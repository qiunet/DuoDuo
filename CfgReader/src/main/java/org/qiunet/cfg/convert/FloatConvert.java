package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2021/11/12 11:03
 */
public class FloatConvert extends BaseObjConvert<Float> {

	@Override
	protected Float fromString0(String str) {
		return Float.parseFloat(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == float.class || type == Float.class;
	}
}
