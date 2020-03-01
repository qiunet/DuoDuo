package org.qiunet.utils.base;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

public abstract class BaseTest {
	protected static final Logger logger = LoggerType.DUODUO.getLogger();

	@BeforeClass
	public static void doInit(){
		logger.info("====================Test Init====================");
	}

	@AfterClass
	public static void doDestory(){
		logger.info("====================Test Destory====================");
	}
}
