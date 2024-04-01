package org.qiunet.utils.convert;

import org.qiunet.utils.convert.enums.Timestamp;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class LongConvert extends BaseObjConvert<Long> {

	@Override
	public Long fromString(Field field, String str) {
		Timestamp annotation = field.getAnnotation(Timestamp.class);
		if (annotation != null) {
			LocalDateTime localDateTime = DateUtil.stringToDate(str);
			ZoneId zoneId = ZoneOffset.UTC;
			if (annotation.zoned()) {
				zoneId = DateUtil.getDefaultZoneId();
			}
			return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
		}

		if (StringUtil.isEmpty(str)) {
			return 0L;
		}
		return Long.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type == Long.class || type == Long.TYPE;
	}
}
