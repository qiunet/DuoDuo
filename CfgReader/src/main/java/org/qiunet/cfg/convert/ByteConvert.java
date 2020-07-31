package org.qiunet.cfg.convert;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class ByteConvert extends BaseObjConvert<Byte> {
	@Override
	protected Byte fromString0(String str) {
		return Byte.valueOf(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Byte.class || type == Byte.TYPE;
	}
}
