package org.qiunet.quartz.test;

import org.junit.Test;
import org.qiunet.quartz.CronSchedule;
import org.qiunet.quartz.QuartzSchedule;
import org.qiunet.utils.classScanner.ClassScanner;
import org.qiunet.utils.logger.LoggerType;

public class TestQuartz {
	@Test
	public void testCron() throws InterruptedException {
		ClassScanner.getInstance().scanner();
		Thread.sleep(100000);
	}


	@CronSchedule("1/5 * * * * ?")
	private void cron() {
		LoggerType.DUODUO.info("i-m cron called");
	}
}
