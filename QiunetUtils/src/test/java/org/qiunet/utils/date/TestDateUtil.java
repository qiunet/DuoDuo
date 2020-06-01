package org.qiunet.utils.date;

import org.junit.Assert;
import org.qiunet.utils.base.BaseTest;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author qiunet
 *         Created on 16/11/5 19:59.
 */
public class TestDateUtil  extends BaseTest{
	@Test
	public void testIsBetweenDay(){
		LocalDateTime date1 = DateUtil.stringToDate("2016-05-16 00:00:00");
		LocalDateTime date2 = DateUtil.stringToDate("2016-05-26 00:00:00");
		LocalDateTime dt1 = DateUtil.stringToDate("2016-05-20 00:00:00");
		LocalDateTime dt2 = DateUtil.stringToDate("2016-05-26 00:00:01");

		Assert.assertTrue(DateUtil.isBetweenDays(dt1, date1, date2));
		Assert.assertFalse(DateUtil.isBetweenDays(dt2, date1, date2));
	}

	/***
	 * SimpleDateFormat 线程安全测试
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	@Test
	public void threadSafeSdf() throws ParseException, InterruptedException {
		String val1  = "2016-05-20 00:00:00";
		String val2  = "2016-05-26 00:00:01";
		LocalDateTime dt1 = DateUtil.stringToDate(val1);
		LocalDateTime dt2 = DateUtil.stringToDate(val2);
		long time1 = DateUtil.getMilliByTime(dt1);
		long time2 = DateUtil.getMilliByTime(dt2);

		CountDownLatch latch = new CountDownLatch(100);
		for (int i = 0; i < 10; i++) {
			new Thread(
					() -> {
						for (int i1 = 0; i1 < 10; i1++) {
							Assert.assertEquals(DateUtil.dateToString(time1), val1);
							Assert.assertEquals(DateUtil.dateToString(time2), val2);

							Assert.assertEquals(DateUtil.getMilliByTime(DateUtil.stringToDate(val1)), time1);
							Assert.assertEquals(DateUtil.getMilliByTime(DateUtil.stringToDate(val2)), time2);

							latch.countDown();
						}
					}
			).start();
		}
		latch.await();
	}

	@Test
	public void testAddHour(){
		int hours = 5;
		LocalDateTime now = DateUtil.currentLocalDateTime();
		Assert.assertTrue((DateUtil.getMilliByTime(now) - (hours * 60 * 60 * 1000)) == DateUtil.getMilliByTime(DateUtil.addHours(now, -hours)));
		Assert.assertTrue((DateUtil.getMilliByTime(now) + (hours * 60 * 60 * 1000)) == DateUtil.getMilliByTime(DateUtil.addHours(now, hours)));
	}

	@Test
	public void testSameDay() throws ParseException {
		LocalDateTime date1 = DateUtil.stringToDate("2016-05-26 00:00:00");
		LocalDateTime date2 = DateUtil.stringToDate("2016-05-26 07:00:00");
		LocalDateTime date3 = DateUtil.stringToDate("2016-05-26 17:00:00");

		Assert.assertTrue(DateUtil.isSameDay(date1, date2));
		Assert.assertTrue(DateUtil.isSameDay(date1, date3));
	}

	@Test
	public void testDateTime() {
		LocalDateTime nowLocalDateTime = DateUtil.nowLocalDateTime();
		LocalDateTime nowLocalDateTimeUTC = DateUtil.nowLocalDateTime(ZoneOffset.UTC);

		long milliByTime = DateUtil.getMilliByTime(nowLocalDateTime);
		long milliByTimeUTC = DateUtil.getMilliByTime(nowLocalDateTimeUTC, ZoneOffset.UTC);

		// 两次调用可能有个1毫秒时间的跨度.
		Assert.assertTrue(Math.abs(milliByTime - milliByTimeUTC) < 2);

		LocalDateTime localDateTime = DateUtil.stringToDate(DateUtil.dateToString(nowLocalDateTime));
		LocalDateTime localDateTimeUTC = DateUtil.stringToDate(DateUtil.dateToString(nowLocalDateTimeUTC));

		long milliByTime1 = DateUtil.getMilliByTime(localDateTime);
		long milliByTime2 = DateUtil.getMilliByTime(localDateTimeUTC, ZoneOffset.UTC);

		Assert.assertEquals(milliByTime1, milliByTime2);
	}

	@Test
	public void testMilliToDateTime() {
		long nowMilliByTime = DateUtil.getNowMilliByTime();
//		System.out.println("当前时间戳:\t" + nowMilliByTime);

		LocalDateTime localDateTime = DateUtil.getLocalDateTime(nowMilliByTime);
		LocalDateTime localDateTimeUTC = DateUtil.getLocalDateTime(nowMilliByTime, ZoneOffset.UTC);

		String dateStr1 = DateUtil.dateToString(localDateTime);
		String dateStr2 = DateUtil.dateToString(localDateTimeUTC);
		String dateStr3 = DateUtil.dateToString(DateUtil.addHours(localDateTimeUTC, 8));

		Assert.assertNotEquals(dateStr1, dateStr2);
		//+8 hour
		Assert.assertEquals(dateStr1, dateStr3);
	}
}
