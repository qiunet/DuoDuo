package org.qiunet.utils.date;

import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.string.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isBetweenDays(Date date,Date date1,Date date2){
		if(date==null||date1==null||date2==null){
			return false;
		}
		if(date1.before(date2)
				&& date.after(date1)
					&& date.before(date2)){
			return true;
		}else if(date.before(date1)
					&& date.after(date2)){
			return true;
		}
		return false;
	}
	/**
	 * 时间的格式验证
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
	/** 之后*/
	private static final int ACTION_AFTER=0;
	/** 之前*/
	private static final int ACTION_BEFORE=1;
	/** 天*/
	private static final int TYPE_DAY=0;
	/** 小时*/
	private static final int TYPE_HOUR=1;
	/** 分钟*/
	private static final int TYPE_MINUTE=2;
	/** 秒*/
	private static final int TYPE_SECOND=3;
	/**
	 * 获取 count(10) type(分钟 小时 天 秒) action(之后 之前) 的日期
	 * @param date
	 * @param type
	 * @param action
	 * @param count
	 * @return
	 */
	private static Date getDate(Date date,int type,int action,int count){
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		count=Math.abs(count);
		switch(type){
		case TYPE_DAY:
			if(action==ACTION_AFTER){
				now.set(Calendar.DATE, now.get(Calendar.DATE) + count);
			}else if(action==ACTION_BEFORE){
				now.set(Calendar.DATE, now.get(Calendar.DATE) - count);
			}
			break;
		case TYPE_HOUR:
			if(action==ACTION_AFTER){
				now.set(Calendar.HOUR, now.get(Calendar.HOUR) + count);
			}else if(action==ACTION_BEFORE){
				now.set(Calendar.HOUR, now.get(Calendar.HOUR) - count);
			}
			break;
		case TYPE_MINUTE:
			if(action==ACTION_AFTER){
				now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + count);
			}else if(action==ACTION_BEFORE){
				now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - count);
			}
			break;
		case TYPE_SECOND:
			if(action==ACTION_AFTER){
				now.set(Calendar.SECOND, now.get(Calendar.SECOND) + count);
			}else if(action==ACTION_BEFORE){
				now.set(Calendar.SECOND, now.get(Calendar.SECOND) - count);
			}
			break;
		}
		return now.getTime();
	}
	/**
	 * 得到某天前几天日期
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date date,int day){
		return getDate(date, TYPE_DAY, ACTION_BEFORE, day);
	}
	/**
	 * 得到某天后几天日期
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date date,int day){
		return getDate(date, TYPE_DAY, ACTION_AFTER, day);
	}
	/**
	 * 得到某天多少小时前的日期
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getHourBefore(Date date,int hour){
		return getDate(date, TYPE_HOUR, ACTION_BEFORE, hour);
	}
	/**
	 * 得到某天多少小时后的日期
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getHourAfter(Date date,int hour){
		return getDate(date, TYPE_HOUR, ACTION_AFTER, hour);
	}
	/**
	 * 得到某天多少分钟后的日期
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getMinuteAfter(Date date,int minute){
		return getDate(date, TYPE_MINUTE, ACTION_AFTER, minute);
	}
	/**
	 * 得到某天多少分钟前的日期
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getMinuteBefore(Date date,int minute){
		return getDate(date, TYPE_MINUTE, ACTION_BEFORE, minute);
	}
	/**
	 * 得到某天多少秒钟前的日期
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date getSecondBefore(Date date,int second){
		return getDate(date, TYPE_SECOND, ACTION_BEFORE, second);
	}
	/**
	 * 得到某天多少秒钟前的日期
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date getSecondAfter(Date date,int second){
		return getDate(date, TYPE_SECOND, ACTION_AFTER, second);
	}

	/***
	 * 是否是同一天
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		return dateToString(d1, DEFAULT_DATE_FORMAT).equals(DateUtil.dateToString(d2, DEFAULT_DATE_FORMAT));
	}
}
