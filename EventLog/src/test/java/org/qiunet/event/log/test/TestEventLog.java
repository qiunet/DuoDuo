package org.qiunet.event.log.test;

import org.junit.Test;
import org.qiunet.event.log.LogRecord;
import org.qiunet.event.log.test.log.M1LogEvent;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:29
 ***/
public class TestEventLog {
	@Test
	public void testLog(){
		LogRecord.sendLog(new M1LogEvent(10000, 1010, 100, "商店购买扣减"));
	}
}
