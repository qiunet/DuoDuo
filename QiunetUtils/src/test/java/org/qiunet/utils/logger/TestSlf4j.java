package org.qiunet.utils.logger;

import org.junit.Test;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 18/2/4
 */
public class TestSlf4j {
	@Test
	public void test(){
		Logger logger = LoggerType.DUODUO.getLogger();
		logger.info(LogUtils.dumpStack("Test"));
	}
}
