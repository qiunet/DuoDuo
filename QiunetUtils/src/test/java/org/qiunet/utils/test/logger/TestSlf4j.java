package org.qiunet.utils.test.logger;

import org.junit.jupiter.api.Test;
import org.qiunet.utils.logger.LogUtils;
import org.qiunet.utils.logger.LoggerType;
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
