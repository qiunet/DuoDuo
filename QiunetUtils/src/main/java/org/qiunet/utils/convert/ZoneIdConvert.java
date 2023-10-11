package org.qiunet.utils.convert;

import java.lang.reflect.Field;
import java.time.ZoneId;

/***
 * ZoneId 转换
 *
 * @author qiunet
 * 2020-03-03 17:58
 ***/
public class ZoneIdConvert extends BaseObjConvert<ZoneId> {
	@Override
	public ZoneId fromString(Field field, String str) {
		return ZoneId.of(str);
	}
}
