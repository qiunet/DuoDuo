package org.qiunet.utils.base;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void doInit(){
		logger.info("====================Test Init====================");
	}

	@After
	public void doDestory(){
		logger.info("====================Test Destory====================");
	}
}
