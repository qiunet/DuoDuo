package org.qiunet.cfg.test.json;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.classScanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 18:26
 ***/
public class TestJsonManager {

	@BeforeClass
	public static void preExec() throws Throwable {
		ClassScanner.getInstance().scanner();
	}
	@Test
	public void test(){
		TestJsonService.instance.testReward();
		TestJsonService.instance.testSimpleMap();
		TestJsonService.instance.testNestMap();
		TestJsonService.instance.testNestList();
	}
}
