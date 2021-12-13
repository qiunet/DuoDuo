package org.qiunet.utils.convert;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.exceptions.CustomException;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 仅仅转换 "yyyy-MM-dd HH:mm:ss" 格式的字符串
 * @author qiunet
 * 2020-02-05 13:55
 **/
public class DateConvert extends BaseObjConvert<Date> {
	private static final ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT));

	@Override
	public Date fromString(Field field, String str) {
		try {
			return sdf.get().parse(str);
		} catch (ParseException e) {
			throw new CustomException(e, "string {} to Date error", str);
		}
	}
}
