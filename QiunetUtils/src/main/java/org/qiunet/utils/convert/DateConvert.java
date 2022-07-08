package org.qiunet.utils.convert;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

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
	private static final ThreadLocal<SimpleDateFormat> datetimeSdf = ThreadLocal.withInitial(() -> new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT));
	private static final ThreadLocal<SimpleDateFormat> dateSdf = ThreadLocal.withInitial(() -> new SimpleDateFormat(DateUtil.DEFAULT_DATE_FORMAT));

	private static final Date ZERO_DATE = new Date(0);
	@Override
	public Date fromString(Field field, String str) {
		if (StringUtil.isEmpty(str)) {
			return ZERO_DATE;
		}
		try {
			if(str.length() > 10) {
				return datetimeSdf.get().parse(str);
			}
			return dateSdf.get().parse(str);
		} catch (ParseException e) {
			throw new CustomException(e, "string {} to Date error", str);
		}
	}
}
