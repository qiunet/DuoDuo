package org.qiunet.logger.sender.test;

import org.junit.Test;
import org.qiunet.logger.sender.LoggerSender;
import org.qiunet.utils.date.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class TestLoggerSender {
	private static final RemoteProperties remote = RemoteProperties.getInstance();
	private static final LoggerSender sender = new LoggerSender(
			remote.getString("remoteIp"),
			remote.getInt("remotePort"),
			remote.getShort("gameId"),
			remote.getString("secret"),
			1);

	@Test
	public void testLogger() throws InterruptedException {
		/*StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100; i++) {
//			sender.sendLog("ItemLog", "qiunet|sss|yyy"+i);
//			if(sb.length() > 0){
//				sb.append("\n");
//			}
			String msg = "{\"id:\":" + i + "}\n";
			sb.append(msg);

			sender.sendImportantLog("RechargeLog", msg);
//			Thread.sleep(10);
		}*/
//		sender.sendImportantLog("RechargeLog", sb.toString());

		List<String> msgList = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
//			sender.sendLog("ItemLog", "zj|sss|yyy"+i);
			msgList.add(i + 1 + "-zhengjian|sss|yyyakjsdghfhklsagdfkljaskjdfhaskfhdsafhhfyyyakjsdghfhklsagdfkljaskjdfhaskfhdsafhhfyyyakjsdghfhklsagdfkljaskjdfhaskfhdsafhhfyyyakjsdghfhklsagdfkljaskjdfhaskfhdsafhhfyyyakjsdghfhklsagdfkljaskjdfhaskfhdsafhhf\n");
		}
		long start = DateUtil.currentTimeMillis();
		sender.sendImportantLog("RechargeLog", msgList);
		long end = DateUtil.currentTimeMillis();
		Thread.sleep(3000);
		System.out.println("time:" + (end - start));
	}
}
