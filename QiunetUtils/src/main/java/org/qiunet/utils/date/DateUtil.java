package org.qiunet.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.string.StringUtil;

/**
 * 时间date相关的工具类
 * @author qiunet
 *
 */
public final class DateUtil {
	private static final  int[] calendars = {Calendar.SECOND,Calendar.MINUTE,Calendar.HOUR_OF_DAY,Calendar.DAY_OF_MONTH,Calendar.MONTH,Calendar.DAY_OF_WEEK};
	/**对应  calendar 中的SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY 是中国的周几*/
	public static final Map<Integer, Byte> DAY_OF_WEEK = new HashMap<Integer, Byte>(){
		private static final long serialVersionUID = -4941953324010074815L;
		{
			put(Calendar.MONDAY, (byte)1);
			put(Calendar.TUESDAY, (byte)2);
			put(Calendar.WEDNESDAY, (byte)3);
			put(Calendar.THURSDAY, (byte)4);
			put(Calendar.FRIDAY, (byte)5);
			put(Calendar.SATURDAY, (byte)6);
			put(Calendar.SUNDAY, (byte)7);
		}
	};

	/***
	 * 当前的秒
	 * @return
	 */
	public static long currSeconds(){
		return System.currentTimeMillis()/1000;
	}

	private DateUtil(){}

	/**反查询周几是 calender的时间*/
	public static final Map<Integer, Integer> REDAY_OF_WEEK = new HashMap<Integer, Integer>(){
		private static final long serialVersionUID = -4941953324010074815L;
		{
			put(1,Calendar.MONDAY);
			put(2,Calendar.TUESDAY);
			put(3,Calendar.WEDNESDAY);
			put(4,Calendar.THURSDAY);
			put(5,Calendar.FRIDAY);
			put(6,Calendar.SATURDAY);
			put(7,Calendar.SUNDAY);
		}
	};

	/**默认的时间格式(日期 时间)*/
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**默认的时间格式(日期)*/
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	/** 时分秒的 */
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	public static final long DAY_MS = 24 * 3600 * 1000;
	public static final long WEEK_MS = 7L * DAY_MS;

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
	 * 获取系统时间，毫秒级
	 * @return
	 */
	public static long getCurrentTimeMillis(){
		// 万一要微调 可以在这个地方修改
		return System.currentTimeMillis();
	}
	/**
	 * 日期转字符串 指定格式
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 字符串转日期 按指定格式
	 * @param stringValue
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String stringValue,String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(stringValue);
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
	 * 时间的格式验证
	 * @param quartzs
	 * @param dt
	 * @return
	 */
	public static  boolean anyCheckDate(String [] quartzs,Date dt){
		boolean ret = true;
		if(quartzs == null || quartzs.length == 0)return false;
		if(quartzs.length != 6){
			throw new ArrayIndexOutOfBoundsException("date quartz string error!!");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		/*验证*/
		for(int i = 0 ; i < quartzs.length; i++){
			if(!quartzs[i].equals("*")){
				int curr = c.get(calendars[i]);
				if(calendars[i] == Calendar.MONTH)curr++;
				if(calendars[i] == Calendar.DAY_OF_WEEK) curr = DAY_OF_WEEK.get(curr);
				if(quartzs[i].indexOf(",") != -1){
					String datas [] = quartzs[i].split(",");
					if(!CommonUtil.existInList(curr+"",datas)){
						ret = false;
					}
				}else if(quartzs[i].indexOf("-") != -1){
					String datas [] = quartzs[i].split("-");
					if(curr < Integer.parseInt(datas[0]) || curr > Integer.parseInt(datas[1])){
						ret = false;
					}
				}else {
					int datas = Integer.parseInt(quartzs[i]);
					if(curr != datas){
						ret = false;
					}
				}
				if(! ret) break;
			}
		}
		return ret;
	}
	/**
	 * 时间的格式验证
	 * @param quartzStr
	 * @param dt
	 * @return
	 */
	public static  boolean anyCheckDate(String quartzStr,Date dt){
		if(StringUtil.isEmpty(quartzStr))return false;

		String quartzTimes [] = quartzStr.split(" +");
		return anyCheckDate(quartzTimes, dt);
	}

	/**
	 * 指定时间 加减 天数
	 * @param dt 时间
	 * @param days 天数
	 * @return
	 */
	public static Date addDays(Date dt, int days) {
		return DateUtils.addDays(dt, days);
	}

	/***
	 * 指定时间  加减 小时数
	 * @param dt
	 * @param hours
	 * @return
	 */
	public static Date addHours(Date dt, int hours) {
		return DateUtils.addHours(dt, hours);
	}

	/***
	 * 指定时间  加减 分钟数
	 * @param dt
	 * @param minutes
	 * @return
	 */
	public static Date addMinutes(Date dt, int minutes) {
		return DateUtils.addMinutes(dt, minutes);
	}

	/***
	 * 指定时间  加减 月
	 * @param dt
	 * @param mouths
	 * @return
	 */
	public static Date addMonths(Date dt, int mouths) {
		return DateUtils.addMonths(dt, mouths);
	}
	/***
	 * 指定时间  加减 秒
	 * @param dt
	 * @param seconds
	 * @return
	 */
	public static Date addSeconds(Date dt, int seconds) {
		return DateUtils.addSeconds(dt, seconds);
	}
	/***
	 * 指定时间  加减 毫秒
	 * @param dt
	 * @param milliSeconds
	 * @return
	 */
	public static Date addMilliseconds(Date dt, int milliSeconds) {
		return DateUtils.addMilliseconds(dt, milliSeconds);
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
		return DateUtils.isSameDay(d1, d2);
	}
}
