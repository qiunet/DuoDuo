package org.qiunet.log.record.test;

import org.junit.jupiter.api.Test;
import org.qiunet.log.record.test.log.M1LogRecordMsg;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:29
 ***/
public class TestEventLog {
	@Test
	public void testLog() throws InterruptedException {
		ClassScanner.getInstance(ScannerType.LOG_RECORD).scanner();

		new M1LogRecordMsg(10000, 1010, 100, "商店购买扣减").send();

		Thread.sleep(1000);
	}
}
