package org.qiunet.cfg.convert;

import org.qiunet.utils.date.DateUtil;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/***
 *
 * @author qiunet
 * 2020-02-04 22:35
 **/
public class LocalDateTimeConvert extends BaseObjConvert<LocalDateTime> {

	@Override
	public LocalDateTime fromString(Field field, String str) {
		return DateUtil.stringToDate(str);
	}

}
