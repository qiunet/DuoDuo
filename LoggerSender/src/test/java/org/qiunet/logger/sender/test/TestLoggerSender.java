package org.qiunet.logger.sender.test;

import org.junit.Test;
import org.qiunet.logger.sender.LoggerSender;

public class TestLoggerSender {

	private static final LoggerSender sender = new LoggerSender(RemoteProperties.getInstance().getString("remoteIp"), RemoteProperties.getInstance().getInt("remotePort"),RemoteProperties.getInstance().getShort("gameId"));
	@Test
	public void testLogger() throws InterruptedException {
		for (int i = 0; i < 500; i++) {
			sender.sendLog("ItemLog", "qiunet|sss|yyy"+i);
//			sender.sendImportantLog("RechargeLog", "qiunet|sss|yyy"+i);
		}
		Thread.sleep(10000);
	}
}
