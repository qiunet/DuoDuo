package org.qiunet.utils.logger;

import org.junit.Test;

/**
 * Created by qiunet.
 * 18/2/4
 */
public class TestLogCollecter  {

	@Test
	public void logTest(){
		LogCollecter.getInstance().log("dev.log", "item", 10001, 22, 13);
	}
}
