package org.qiunet.utils.test.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

public abstract class BaseTest {
	protected static final Logger logger = LoggerType.DUODUO.getLogger();

	@BeforeAll
	public static void doInit(){
		logger.info("====================Test Init====================");
	}

	@AfterAll
	public static void doDestory(){
		logger.info("====================Test Destory====================");
	}
}
