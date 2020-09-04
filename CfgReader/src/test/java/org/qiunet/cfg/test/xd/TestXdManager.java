package org.qiunet.cfg.test.xd;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.scanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 18:26
 ***/
public class TestXdManager {

	@BeforeClass
	public static void preExec() throws Throwable {
		ClassScanner.getInstance().scanner();
	}
	@Test
	public void test(){
		TestXdService.instance.testReward();
		TestXdService.instance.testSimpleMap();
		TestXdService.instance.testNestMap();
		TestXdService.instance.testNestList();
	}
}
