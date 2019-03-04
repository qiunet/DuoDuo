package org.qiunet.quartz.test;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.quartz.CronExpressionData;
import org.qiunet.utils.date.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class TestCropressionData {
	private static boolean anyCheckDate(String quartz, Date dt) {
		return new CronExpressionData(quartz).isValid(dt);
	}
	@Test
	public void testAnyCheckDate(){
		String quartz = "* * * * * *";
		Assert.assertTrue(anyCheckDate(quartz, new Date()));

		try {
			Date dt = DateUtil.stringToDate("2016-05-16 23:59:00");
			quartz = "0 * * * * *";
			Assert.assertTrue(anyCheckDate(quartz, dt));

			quartz = "1 * * * * *";
			Assert.assertFalse(anyCheckDate(quartz, dt));

			quartz = "0 59 * * * *";
			Assert.assertTrue(anyCheckDate(quartz, dt));

			quartz = "* * 23 * * *";
			Assert.assertTrue(anyCheckDate(quartz, dt));

			quartz = "* * * 10-19 * *";
			Assert.assertTrue(anyCheckDate(quartz, dt));

			quartz = "0 4/5 * * * *";
			Assert.assertTrue(anyCheckDate(quartz, dt));

			quartz = "0 4-10/5 * * * *";
			Assert.assertFalse(anyCheckDate(quartz, dt));

			quartz = "0 4-10 * * * *";
			Assert.assertFalse(anyCheckDate(quartz, dt));


			quartz = "* * * * 5,6,7 *";
			Assert.assertTrue(anyCheckDate(quartz, dt));

			quartz = "* * * * * 1,7";
			Assert.assertTrue(anyCheckDate(quartz, dt));

			quartz = "0 0 0,12 * * *";
			Assert.assertTrue(anyCheckDate(quartz, DateUtil.stringToDate("2018-10-01 12:00:00")));

			quartz = "* * * L * *";
			Assert.assertFalse(anyCheckDate(quartz, dt));

			dt = new Date();
			Calendar cal = Calendar.getInstance();
			int curr = cal.get(Calendar.DAY_OF_MONTH);
			int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			quartz = "* * * "+(max - curr)+"L * *";
			Assert.assertTrue(anyCheckDate(quartz, dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
