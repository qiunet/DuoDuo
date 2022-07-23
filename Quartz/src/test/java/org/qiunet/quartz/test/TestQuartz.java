package org.qiunet.quartz.test;

import org.junit.jupiter.api.Test;
import org.qiunet.quartz.CronSchedule;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

public class TestQuartz {
	@Test
	public void testCron() throws Exception {
		ClassScanner.getInstance(ScannerType.CRON).scanner();
		Thread.sleep(10000);
	}


	@CronSchedule("0/5 * * * * ?")
	private void cron() {
		LoggerType.DUODUO.info("i-m 5 seconds called");
	}


	@CronSchedule("* * * * * ?")
	private void secondsCron() {
		LoggerType.DUODUO.info("i-m 1 seconds called");
	}
}
