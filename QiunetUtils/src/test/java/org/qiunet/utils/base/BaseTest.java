package org.qiunet.utils.base;

import org.apache.log4j.Logger;
import org.junit.*;

public abstract class BaseTest {
	protected static Logger logger = Logger.getLogger(BaseTest.class);

	@Before
	public void doInit(){
		logger.info("====================Test Init====================");
	}

	@After
	public void doDestory(){
		logger.info("====================Test Destory====================");
	}
}
