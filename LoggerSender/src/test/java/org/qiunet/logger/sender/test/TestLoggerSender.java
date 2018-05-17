package org.qiunet.logger.sender.test;

import org.junit.Test;
import org.qiunet.logger.sender.LoggerSender;

public class TestLoggerSender {
	private static final RemoteProperties remote = RemoteProperties.getInstance();
	private static final LoggerSender sender = new LoggerSender(remote.getString("remoteIp"), remote.getInt("remotePort"),remote.getShort("gameId"), remote.getString("secret"));
	@Test
	public void testLogger() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			sender.sendLog("ItemLog", "qiunet|sss|yyy"+i);
//			sender.sendImportantLog("RechargeLog", "qiunet|sss|yyy"+i);
		}
		Thread.sleep(10000);
	}
}
