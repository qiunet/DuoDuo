package org.qiunet.logger.sender.test;

import org.junit.Test;
import org.qiunet.logger.sender.LoggerSender;

public class TestLoggerSender {

	private static final LoggerSender sender = new LoggerSender(RemoteProperties.getInstance().getString("remoteIp"), RemoteProperties.getInstance().getInt("remotePort"),RemoteProperties.getInstance().getShort("gameId"));
	@Test
	public void testLogger() throws InterruptedException {
		sender.sendLog("ItemLog", "qiunet|sss|yyy");
		sender.sendImportantLog("RechargeLog", "qiunet|sss|yyy");
		Thread.sleep(5000);
	}
}
