package org.qiunet.utils.date;

import org.junit.Assert;
import org.qiunet.utils.base.BaseTest;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author qiunet
 *         Created on 16/11/5 19:59.
 */
public class TestDateUtil  extends BaseTest{
	@Test
	public void testIsBetweenDay(){
		try {
			Date date1 = DateUtil.stringToDate("2016-05-16 00:00:00");
			Date date2 = DateUtil.stringToDate("2016-05-26 00:00:00");
			Date dt1 = DateUtil.stringToDate("2016-05-20 00:00:00");
			Date dt2 = DateUtil.stringToDate("2016-05-26 00:00:01");

			Assert.assertTrue(DateUtil.isBetweenDays(dt1, date1, date2));
			Assert.assertFalse(DateUtil.isBetweenDays(dt2, date1, date2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testAddHour(){
		int hours = 5;
		Date now = new Date();
		Assert.assertTrue((now.getTime() - (hours * 60 * 60 * 1000)) == DateUtil.addHours(now, -hours).getTime());
		Assert.assertTrue((now.getTime() + (hours * 60 * 60 * 1000)) == DateUtil.addHours(now, hours).getTime());
	}

	@Test
	public void testSameDay() throws ParseException {
		Date date1 = DateUtil.stringToDate("2016-05-26 00:00:00");
		Date date2 = DateUtil.stringToDate("2016-05-26 07:00:00");
		Date date3 = DateUtil.stringToDate("2016-05-26 17:00:00");

		Assert.assertTrue(DateUtil.isSameDay(date1, date2));
		Assert.assertTrue(DateUtil.isSameDay(date1, date3));
	}
	@Test
	public void testAnyCheckDate(){
		String quartz = "* * * * * *";
		Assert.assertTrue(DateUtil.anyCheckDate(quartz, new Date()));

		try {
			Date dt = DateUtil.stringToDate("2016-05-16 23:59:00");
			quartz = "0 * * * * *";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));

			quartz = "1 * * * * *";
			Assert.assertFalse(DateUtil.anyCheckDate(quartz, dt));

			quartz = "0 59 * * * *";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));

			quartz = "* * 23 * * *";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));

			quartz = "* * * 10-19 * *";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));

			quartz = "0 4/5 * * * *";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));

			quartz = "0 4-10/5 * * * *";
			Assert.assertFalse(DateUtil.anyCheckDate(quartz, dt));

			quartz = "0 4-10 * * * *";
			Assert.assertFalse(DateUtil.anyCheckDate(quartz, dt));


			quartz = "* * * * 5,6,7 *";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));

			quartz = "* * * * * 1,7";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));

			quartz = "* * * L * *";
			Assert.assertFalse(DateUtil.anyCheckDate(quartz, dt));

			dt = new Date();
			Calendar cal = Calendar.getInstance();
			int curr = cal.get(Calendar.DAY_OF_MONTH);
			int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			quartz = "* * * "+(max - curr)+"L * *";
			Assert.assertTrue(DateUtil.anyCheckDate(quartz, dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
