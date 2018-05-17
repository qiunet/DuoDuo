package org.qiunet.quartz.test;

import org.junit.Test;
import org.qiunet.quartz.QuartzSchedule;

public class TestQuartz {
	@Test
	public void testQuartz() throws InterruptedException {
		QuartzSchedule.getInstance().addJob(new SecondJob());
		QuartzSchedule.getInstance().addJob(new ArraySecondJob());

		Thread.sleep(100000);
	}
}
