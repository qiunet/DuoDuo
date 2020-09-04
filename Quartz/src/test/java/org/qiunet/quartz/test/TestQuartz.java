package org.qiunet.quartz.test;

import org.junit.Test;
import org.qiunet.quartz.CronSchedule;
import org.qiunet.scanner.ClassScanner;
import org.qiunet.utils.logger.LoggerType;

public class TestQuartz {
	@Test
	public void testCron() throws Exception {
		ClassScanner.getInstance().scanner();
		Thread.sleep(10000);
	}


	@CronSchedule("0/5 * * * * ?")
	private void cron() {
		LoggerType.DUODUO.info("i-m cron called");
	}


	@CronSchedule("* * * * * ?")
	private void secondsCron() {
		LoggerType.DUODUO.info("i-m seconds called");
	}
}
