package org.qiunet.excel2cfgs.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2021/11/15 14:19
 */
public class DateUtil {
	private static final ZoneId defaultZoneId = ZoneId.systemDefault();


	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 外面直接使用 LocalDateTime.format() 可以搞定
	 **/
	public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

	private static final Map<String, DateTimeFormatter> dtfs = new HashMap() {
		{
			put(DEFAULT_DATE_TIME_FORMAT, DEFAULT_DATE_TIME_FORMATTER);
		}
	};

	public static String dateToString(long millis) {
		return dateToString(millis, DEFAULT_DATE_TIME_FORMAT);
	}
	/**
	 * 日期转字符串 指定格式
	 *
	 * @param millis
	 * @param format
	 * @return
	 */
	public static String dateToString(long millis, String format) {
		return returnFormatter(format).format(LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), defaultZoneId));
	}

	/**
	 * 使用LocalDateTime格式化时间. 取到对应的 DateTimeFormatter
	 *
	 * @param pattern
	 * @return
	 */
	public static DateTimeFormatter returnFormatter(String pattern) {
		if (!dtfs.containsKey(pattern)) {
			synchronized (DateUtil.class) {
				if (!dtfs.containsKey(pattern)) {
					dtfs.put(pattern, DateTimeFormatter.ofPattern(pattern));
				}
			}
		}
		return dtfs.get(pattern);
	}


	public static LocalDateTime stringToDate(String string) {
		return stringToDate(string, DEFAULT_DATE_TIME_FORMAT);
	}

	/**
	 * 字符串转日期 按指定格式
	 *
	 * @param stringValue
	 * @param format
	 * @return
	 */
	public static LocalDateTime stringToDate(String stringValue, String format) {
		return LocalDateTime.parse(stringValue, returnFormatter(format));
	}

	public static long getMilliByTime(LocalDateTime localDateTime) {
		return localDateTime.atZone(defaultZoneId).toInstant().toEpochMilli();
	}
}
