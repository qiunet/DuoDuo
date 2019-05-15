package org.qiunet.utils.base;

import org.junit.*;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
