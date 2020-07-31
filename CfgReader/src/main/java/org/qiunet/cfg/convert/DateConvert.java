package org.qiunet.cfg.convert;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;

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
	protected Date fromString0(String str) {
		try {
			return sdf.get().parse(str);
		} catch (ParseException e) {
			LoggerType.DUODUO_CFG_READER.error("Exception:", e);
		}
		return null;
	}

	@Override
	public boolean canConvert(Class type) {
		return Date.class == type;
	}
}
