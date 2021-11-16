package org.qiunet.cfg.convert;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

/***
 *
 *
 * @author qiunet
 * 2020-03-03 17:58
 ***/
public class CharsetConvert extends BaseObjConvert<Charset> {
	@Override
	public Charset fromString(Field field, String str) {
		return Charset.forName(str);
	}
}
