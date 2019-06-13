package org.qiunet.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.*;

/**
 * 时间date相关的工具类
 * 也可以使用DateTimeFormatter 但是使用线程变量已经可以解决问题. 就不再修改.
 * @author qiunet
 *
 */
public final class DateUtil {
	private static final ThreadLocal<Map<String, SimpleDateFormat>> simpleDataFormatThreadLocal = new ThreadLocal<>();
	private static final ZoneId defaultZoneId = ZoneId.systemDefault();
	private static long offsetMillis = 0;
	/***
	 * 当前的秒
	 * @return
	 */
	public static long currSeconds(){
		return DateUtil.currentTimeMillis()/1000;
	}
	/**
	 * 得到当前的 Instant
	 * @return
	 */
	public static Instant currentInstant() {
		return Instant.ofEpochMilli(currentTimeMillis());
	}

	public static LocalDateTime currentLocalDateTime() {
		return LocalDateTime.ofInstant(currentInstant(), defaultZoneId);
	}

	public static ZoneId getDefaultZoneId(){
		return defaultZoneId;
	}

	public static long currentTimeMillis() {
		return System.currentTimeMillis() + offsetMillis;
	}
	/***
	 * 对全局时间偏移做调整
	 * @param offsetMillis
	 */
	public static void setOffsetMillis(long offsetMillis) {
		DateUtil.offsetMillis = offsetMillis;
	}

	private DateUtil(){}

	/**默认的时间格式(日期 时间)*/
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**默认的时间格式(日期)*/
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	/** 时分秒的 */
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

	public static final long DAY_MS = 24 * 3600 * 1000;

	public static final long WEEK_MS = 7L * DAY_MS;

	/**外面直接使用 LocalDateTime.format() 可以搞定**/
	public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

	/**
	 * 日期转字符串 默认格式
	 *
	 * @param millis
	 * @return
	 */
	public static String dateToString(long millis) {
		return dateToString(millis, DEFAULT_DATE_TIME_FORMAT);
	}

	/***
	 * 格式化为 yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String dateToString(LocalDateTime date) {
		return DEFAULT_DATE_TIME_FORMATTER.format(date);
	}

	//获取指定日期的毫秒
	public static long getMilliByTime(LocalDateTime time) {
		return time.atZone(getDefaultZoneId()).toInstant().toEpochMilli();
	}

	//获取指定日期的秒
	public static long getSecondsByTime(LocalDateTime time) {
		return time.atZone(getDefaultZoneId()).toInstant().getEpochSecond();
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

	public static String dateToString(LocalDateTime date, String format) {
		return returnFormatter(format).format(date);
	}
	/**
	 * 字符串转日期 按指定格式
	 * @param stringValue
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static LocalDateTime stringToDate(String stringValue,String format){
		return LocalDateTime.parse(stringValue,  returnFormatter(format));
	}
	/**
	 * 字符串转日期 按默认格式
	 * @param stringValue
	 * @return
	 * @throws ParseException
	 */
	public static LocalDateTime stringToDate(String stringValue){
		return stringToDate(stringValue, DEFAULT_DATE_TIME_FORMAT);
	}
	/**
	 * 获取当前日期是本周的周几
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	/**
	 * 获取当前日期是今年的第几周
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * 是否介于两个日期之间
	 * @param date
	 * @param dateBefore
	 * @param dateLast
	 * @return
	 */
	public static boolean isBetweenDays(LocalDateTime date,
										LocalDateTime dateBefore,
										LocalDateTime dateLast){
		long d = getMilliByTime(date);
		long d1 = getMilliByTime(dateBefore);
		long d2 = getMilliByTime(dateLast);

		return d >= d1 && d < d2;
	}

	/**
	 * 指定时间 加减 天数
	 * @param dt 时间
	 * @param days 天数
	 * @return
	 */
	public static LocalDateTime addDays(LocalDateTime dt, int days) {
		return dt.plusDays(days);
	}

	/***
	 * 指定时间  加减 小时数
	 * @param dt
	 * @param hours
	 * @return
	 */
	public static LocalDateTime addHours(LocalDateTime dt, int hours) {
		return dt.plusHours(hours);
	}

	/***
	 * 指定时间  加减 分钟数
	 * @param dt
	 * @param minutes
	 * @return
	 */
	public static LocalDateTime addMinutes(LocalDateTime dt, int minutes) {
		return dt.plusMinutes(minutes);
	}

	/***
	 * 指定时间  加减 月
	 * @param dt
	 * @param months
	 * @return
	 */
	public static LocalDateTime addMonths(LocalDateTime dt, int months) {
		return dt.plusMonths(months);
	}
	/***
	 * 指定时间  加减 秒
	 * @param dt
	 * @param seconds
	 * @return
	 */
	public static LocalDateTime addSeconds(LocalDateTime dt, int seconds) {
		return dt.plusSeconds(seconds);
	}
	/***
	 * 指定时间  加减 毫秒
	 * @param dt
	 * @param milliSeconds
	 * @return
	 */
	public static LocalDateTime addMilliseconds(LocalDateTime dt, int milliSeconds) {
		return dt.plus(milliSeconds, ChronoUnit.MILLIS);
	}
	/***
	 * 判断是否是同一天
	 * @param ld1
	 * @param ld2
	 * @return
	 */
	public static boolean isSameDay(LocalDateTime ld1, LocalDateTime ld2) {
		return  ld1.getYear() == ld2.getYear() &&
			ld1.getDayOfYear() == ld2.getDayOfYear();
	}
	/***
	 * 判断是否是同一天
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(long d1, long d2) {
		LocalDateTime ld1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(d1), defaultZoneId);
		LocalDateTime ld2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(d2), defaultZoneId);

		return isSameDay(ld1, ld2);
	}

	private static Map<String, DateTimeFormatter> dtfs = new HashMap(){
		{
			put(DEFAULT_DATE_TIME_FORMAT, DEFAULT_DATE_TIME_FORMATTER);
		}
	};

	/**
	 * 使用LocalDateTime格式化时间. 取到对应的 DateTimeFormatter
	 * @param pattern
	 * @return
	 */
	public static DateTimeFormatter returnFormatter(String pattern){
		if (! dtfs.containsKey(pattern)) {
			synchronized (DateUtil.class) {
				if (! dtfs.containsKey(pattern)) {
					dtfs.put(pattern, DateTimeFormatter.ofPattern(pattern));
				}
			}
		}
		return dtfs.get(pattern);
	}
}
