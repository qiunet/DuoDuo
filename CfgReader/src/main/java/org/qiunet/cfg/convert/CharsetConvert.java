package org.qiunet.cfg.convert;

import java.nio.charset.Charset;

/***
 *
 *
 * @author qiunet
 * 2020-03-03 17:58
 ***/
public class CharsetConvert extends BaseObjConvert<Charset> {
	@Override
	protected Charset fromString0(String str) {
		return Charset.forName(str);
	}

	@Override
	public boolean canConvert(Class aClass) {
		return aClass == Charset.class;
	}
}
