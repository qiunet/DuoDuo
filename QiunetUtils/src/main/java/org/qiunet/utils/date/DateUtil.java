package org.qiunet.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间date相关的工具类
 * 也可以使用DateTimeFormatter 但是使用线程变量已经可以解决问题. 就不再修改.
 * @author qiunet
 *
 */
public final class DateUtil {
	private static final ThreadLocal<Map<String, SimpleDateFormat>> simpleDataFormatThreadLocal = new ThreadLocal<>();
	/***
	 * 当前的秒
	 * @return
	 */
	public static long currSeconds(){
		return System.currentTimeMillis()/1000;
	}

	private static SimpleDateFormat returnSdf(String format) {
		Map<String, SimpleDateFormat> map = simpleDataFormatThreadLocal.get();
		if (map == null) {
			map = new HashMap<>();
			simpleDataFormatThreadLocal.set(map);
		}
		SimpleDateFormat sdf = map.get(format);
		if (sdf == null) {
			sdf = new SimpleDateFormat(format);
			map.put(format, sdf);
		}
		return sdf;
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
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		if(date == null) return "";
		return dateToString(date, DEFAULT_DATE_TIME_FORMAT);
	}
	/**
	 * 日期转字符串 指定格式
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		return returnSdf(format).format(date);
	}
	/**
	 * 字符串转日期 按指定格式
	 * @param stringValue
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String stringValue,String format) throws ParseException{
		return returnSdf(format).parse(stringValue);
	}
	/**
	 * 字符串转日期 按默认格式
	 * @param stringValue
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String stringValue) throws ParseException{
		return stringToDate(stringValue,DEFAULT_DATE_TIME_FORMAT);
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
	public static boolean isBetweenDays(Date date,Date dateBefore,Date dateLast){
		long d = date.getTime();
		long d1 = dateBefore.getTime();
		long d2 = dateLast.getTime();

		return d >= d1 && d < d2;
	}

	/**
	 * 指定时间 加减 天数
	 * @param dt 时间
	 * @param days 天数
	 * @return
	 */
	public static Date addDays(Date dt, int days) {
		return dateAdjust(dt, days, Calendar.DAY_OF_MONTH);
	}

	/***
	 * 指定时间  加减 小时数
	 * @param dt
	 * @param hours
	 * @return
	 */
	public static Date addHours(Date dt, int hours) {
		return dateAdjust(dt, hours, Calendar.HOUR_OF_DAY);
	}

	/***
	 * 指定时间  加减 分钟数
	 * @param dt
	 * @param minutes
	 * @return
	 */
	public static Date addMinutes(Date dt, int minutes) {
		return dateAdjust(dt, minutes, Calendar.MINUTE);
	}

	/***
	 * 指定时间  加减 月
	 * @param dt
	 * @param months
	 * @return
	 */
	public static Date addMonths(Date dt, int months) {
		return dateAdjust(dt, months, Calendar.MONTH);
	}
	/***
	 * 指定时间  加减 秒
	 * @param dt
	 * @param seconds
	 * @return
	 */
	public static Date addSeconds(Date dt, int seconds) {
		return dateAdjust(dt, seconds, Calendar.SECOND);
	}
	/***
	 * 指定时间  加减 毫秒
	 * @param dt
	 * @param milliSeconds
	 * @return
	 */
	public static Date addMilliseconds(Date dt, int milliSeconds) {
		return dateAdjust(dt, milliSeconds, Calendar.MILLISECOND);
	}

	private static Date dateAdjust(Date dt, int count, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(field, count);
		return calendar.getTime();
	}
	/**
	 *获取两个日期相差的秒数
	 * @return
     */
	public static Long getDiffSecond(Date endDate){
		Date current = new Date();
		return getDiffSecond(current, endDate);
	}

	public static Long getDiffSecond(Date beginDate, Date endDate){
		return (endDate.getTime() - beginDate.getTime())/1000;
	}

	/***
	 * 判断是否是同一天
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}

		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
				cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
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
