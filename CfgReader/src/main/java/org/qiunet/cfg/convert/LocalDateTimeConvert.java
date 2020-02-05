package org.qiunet.cfg.convert;

import org.qiunet.utils.date.DateUtil;

import java.time.LocalDateTime;

/***
 *
 * @author qiunet
 * 2020-02-04 22:35
 **/
public class LocalDateTimeConvert extends BaseObjConvert<LocalDateTime> {

	@Override
	protected LocalDateTime fromString0(String str) {
		return DateUtil.stringToDate(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return LocalDateTime.class == type;
	}
}
