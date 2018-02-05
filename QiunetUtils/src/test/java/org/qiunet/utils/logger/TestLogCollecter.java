package org.qiunet.utils.logger;

import org.junit.Ignore;

/**
 * Created by qiunet.
 * 18/2/4
 */
public class TestLogCollecter  {

	@Ignore
	public void logTest(){
		LogCollecter.getInstance().log("dev.log", "item", 10001, 22, 13);
	}
}
