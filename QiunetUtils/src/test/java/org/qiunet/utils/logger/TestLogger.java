package org.qiunet.utils.logger;

import org.junit.Test;
import org.qiunet.utils.base.BaseTest;
import org.qiunet.utils.logger.base.TheGameLoggerUtil;

/**
 * @author qiunet
 *         Created on 17/1/6 12:00.
 */
public class TestLogger extends BaseTest{
	
	@Test
	public void loggerLoop() throws InterruptedException {
		TheGameLoggerUtil.startAppenderLoggerToList();
		
		for (int i = 0 ; i < 5; i++) {
			String msg = i+"我们\n是中国人abc\ndccc";
			if (i < 3)
				logger.info(msg);
			else if (i == 3)
				logger.error(msg);
			else
				logger.debug(msg);
		}
		
		TheGameLoggerUtil.printAllLoggerNormal();
	}
//	@Test
//	public void loggerException() throws InterruptedException {
//		logger.info("我们\n是中国人abc\ndccc", new NullPointerException("Test"));
//	}
}
