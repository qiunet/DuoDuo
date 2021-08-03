package org.qiunet.logger.sender.test;

import org.junit.Test;
import org.qiunet.logger.sender.LoggerSender;

public class TestLoggerSender {
	private static final RemoteProperties remote = RemoteProperties.getInstance();
	private static final LoggerSender sender = new LoggerSender(remote.getString("remoteIp"), remote.getInt("remotePort"), remote.getShort("gameId"), remote.getString("secret"));

	@Test
	public void testLogger() throws InterruptedException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100; i++) {
//			sender.sendLog("ItemLog", "qiunet|sss|yyy"+i);
//			if(sb.length() > 0){
//				sb.append("\n");
//			}
			String msg = "{\"id:\":" + i + "}\n";
			sb.append(msg);

			sender.sendImportantLog("RechargeLog", msg);
//			Thread.sleep(10);
		}
//		sender.sendImportantLog("RechargeLog", sb.toString());

		Thread.sleep(1000);
	}
}
