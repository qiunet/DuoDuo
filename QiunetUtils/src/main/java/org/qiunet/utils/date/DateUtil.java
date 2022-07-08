package org.qiunet.utils.date;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.qiunet.utils.common.IRunnable;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 时间date相关的工具类
 * 也可以使用DateTimeFormatter 但是使用线程变量已经可以解决问题. 就不再修改.
 *
 * @author qiunet
 */
public final class DateUtil {
	private static final ZoneId defaultZoneId = ZoneId.systemDefault();

	/**
	 * 默认的时间格式(日期 时间)
	 */
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认日期格式
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * 外面直接使用 LocalDateTime.format() 可以搞定
	 **/
	public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT).withLocale(Locale.getDefault());

	/**
	 * 一天的秒
	 */
	public static final long DAY_SECONDS = 24 * 3600;
	/**
	 * 一天的毫秒数
	 */
	public static final long DAY_MS = DAY_SECONDS * 1000;
	/**
	 * 一周的毫秒数
	 */
	public static final long WEEK_SECONDS = 7L * DAY_SECONDS;
	/**
	 * 一周的毫秒数
	 */
	public static final long WEEK_MS = WEEK_SECONDS * 1000;

	/**
	 * 服务器时间偏移量
	 */
	private static long offsetMillis; //86400000

	/***
	 * 当前的秒
	 * @return
	 */
	public static long currSeconds() {
		return currentTimeMillis() / 1000;
	}

	/**
	 * 得到当前的 Instant
	 *
	 * @return
	 */
	public static Instant currentInstant() {
		return Instant.ofEpochMilli(currentTimeMillis());
	}
	/**
	 * 当前时间
	 *
	 * @return
	 */
	public static LocalDateTime nowLocalDateTime() {
		return nowLocalDateTime(getDefaultZoneId());
	}

	/**
	 * 当前时间
	 *
	 * @param zoneId
	 * @return
	 */
	public static LocalDateTime nowLocalDateTime(ZoneId zoneId) {
		return LocalDateTime.ofInstant(currentInstant(), zoneId);
	}
	/**
	 * 获得默认区 ID
	 * @return
	 */
	public static ZoneId getDefaultZoneId() {
		return defaultZoneId;
	}

	/***
	 * 对全局时间偏移做调整
	 * @param offsetValue
	 */
	public static void setTimeOffset(long offsetValue, TimeUnit unit) {
		DateUtil.offsetMillis = unit.toMillis(offsetValue);
	}

	private DateUtil() {
	}
	/**
	 * 计算Runnable 消耗毫秒
	 * @param runnable 执行代码
	 * @return 毫秒
	 */
	public static long calConsumeMillisSeconds(IRunnable runnable) throws Exception{
		long start = System.currentTimeMillis();
		runnable.run();
		return System.currentTimeMillis() - start;
	}
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

	/**
	 * 获取指定日期的毫秒
	 * @param time
	 * @return
	 */
	public static long getMilliByTime(LocalDateTime time) {
		return time.atZone(getDefaultZoneId()).toInstant().toEpochMilli();
	}

	/**
	 * 获取指定日期的毫秒
	 * @param time
	 * @return
	 */
	public static long getMilliByTime(LocalDateTime time, ZoneOffset offset) {
		return time.toInstant(offset).toEpochMilli();
	}

	/**
	 * 获取指定日期的秒
	 * @param time
	 * @return
	 */
	public static long getSecondsByTime(LocalDateTime time) {
		return time.atZone(getDefaultZoneId()).toInstant().getEpochSecond();
	}

	/**
	 * 根据时间戳 得到LocalDateTime
	 *
	 * @param milliseconds
	 * @return
	 */
	public static LocalDateTime getLocalDateTime(long milliseconds) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), getDefaultZoneId());
	}

	/**
	 * 根据时间戳 得到LocalDateTime
	 * @param milliseconds
	 * @param zoneOffset
	 * @return
	 */
	public static LocalDateTime getLocalDateTime(long milliseconds, ZoneOffset zoneOffset) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), zoneOffset);
	}
	/**
	 * 日期转字符串 指定格式
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		return dateToString(date.getTime(), format);
	}
	/**
	 * 日期转字符串 指定格式
	 *
	 * @param millis
	 * @param format
	 * @return
	 */
	public static String dateToString(long millis, String format) {
		return dateToString(getLocalDateTime(millis), format);
	}

	/**
	 * 日期转时间
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(LocalDateTime date, String format) {
		return returnFormatter(format).format(date);
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

	/**
	 * 字符串转日期 按默认格式
	 *
	 * @param stringValue
	 * @return
	 */
	public static LocalDateTime stringToDate(String stringValue) {
		return stringToDate(stringValue, DEFAULT_DATE_TIME_FORMAT);
	}
	/**
	 * 是否介于两个日期之间
	 *
	 * @param date
	 * @param dateBefore
	 * @param dateLast
	 * @return
	 */
	public static boolean isBetweenDays(LocalDateTime date,
										LocalDateTime dateBefore,
										LocalDateTime dateLast) {
		long d = getMilliByTime(date);
		long d1 = getMilliByTime(dateBefore);
		long d2 = getMilliByTime(dateLast);

		return d >= d1 && d < d2;
	}

	/**
	 * @return 获取当前纪元毫秒 1970-01-01T00:00:00Z.
	 */
	public static long currentTimeMillis() {
		return Clock.systemDefaultZone().instant().toEpochMilli() + offsetMillis;
	}

	/**@
	 * 判断两个时间是否在同一天
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(LocalDateTime time1, LocalDateTime time2) {
		return time1.getYear() == time2.getYear() &&
				time1.getDayOfYear() == time2.getDayOfYear();
	}
	/**@
	 * 判断两个时间是否在同一天
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(long time1, long time2) {
		return Duration.ofMillis(time1).toDays() - Duration.ofMillis(time2).toDays() == 0;
	}

	/**
	 * 判断两个时间是否在同一周(注意这里周日和周一判断是在一周里的)
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameWeek(long time1, long time2) {
		LocalDateTime ldt1 = getLocalDateTime(time1);
		LocalDateTime ldt2 = getLocalDateTime(time2);
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		return ldt1.getYear() == ldt2.getYear() && ldt1.get(woy) == ldt2.get(woy);
	}

	/**@
	 * 判断两个时间是否在同一月
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameMonth(long time1, long time2) {
		LocalDateTime ldt1 = getLocalDateTime(time1);
		LocalDateTime ldt2 = getLocalDateTime(time2);
		return ldt1.getYear() == ldt2.getYear() && ldt1.getMonthValue() == ldt2.getMonthValue();
	}

	/**@
	 * 判断两个时间是否在同一季度
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameQuarter(long time1, long time2) {
		LocalDateTime ldt1 = getLocalDateTime(time1);
		LocalDateTime ldt2 = getLocalDateTime(time2);
		return ldt1.getYear() == ldt2.getYear() && ldt1.getMonthValue() / 4 == ldt2.getMonthValue() / 4;
	}

	/**@
	 * 判断两个时间是否在同一年
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameYear(long time1, long time2) {
		return getLocalDateTime(time1).getYear() == getLocalDateTime(time2).getYear();
	}
	/**@
	 * 获取传入毫秒的月份中的日 1-12
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfMonth(long time) {
		return getLocalDateTime(time).getDayOfMonth();
	}

	/**@
	 * 获取系统当前月份中的日 1-12
	 *
	 * @return
	 */
	public static int getDayOfMonth() {
		return getDayOfMonth(currentTimeMillis());
	}

	/**
	 * 获取传入毫秒的天 1 to 365, or 366
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfYear(long time) {
		return getLocalDateTime(time).getDayOfYear();
	}

	/**
	 * 获取系统当前的天 1 to 365, or 366
	 *
	 * @return
	 */
	public static int getDayOfYear() {
		return getDayOfYear(currentTimeMillis());
	}
	/**
	 * 获取系统当前的星期 1-7
	 *
	 * @return
	 */
	public static int getDayOfWeek(long time) {
		return getLocalDateTime(time).getDayOfWeek().getValue();
	}
	/**
	 * 获取系统当前的星期 1-7
	 *
	 * @return
	 */
	public static int getDayOfWeek() {
		return getDayOfWeek(currentTimeMillis());
	}

	private static final Map<String, DateTimeFormatter> DATE_FORMATS = Maps.newHashMap(ImmutableMap.of(DEFAULT_DATE_TIME_FORMAT, DEFAULT_DATE_TIME_FORMATTER));
	/**
	 * 使用LocalDateTime格式化时间. 取到对应的 DateTimeFormatter
	 *
	 * @param pattern
	 * @return
	 */
	public static DateTimeFormatter returnFormatter(String pattern) {
		DateTimeFormatter formatter = DATE_FORMATS.get(pattern);
		if (formatter == null) {
			synchronized (DateUtil.class) {
				formatter = DATE_FORMATS.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
			}
		}
		return formatter;
	}

	/**
	 * 是否是闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeap(int year) {
		return Year.isLeap(year);
	}
	/**
	 * 获得最后天数
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getLastDayOfMonth(int year, int month) {
		return YearMonth.of(year, month).lengthOfMonth();
	}
}
