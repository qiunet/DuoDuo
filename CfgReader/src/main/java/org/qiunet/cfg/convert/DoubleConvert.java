package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2020-02-04 14:57
 **/
public class DoubleConvert extends BaseObjConvert<Double> {
	@Override
	protected Double fromString0(String str) {
		return Double.valueOf(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Double.TYPE || type == Double.class;
	}
}
