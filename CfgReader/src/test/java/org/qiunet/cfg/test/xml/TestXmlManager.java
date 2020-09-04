package org.qiunet.cfg.test.xml;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.scanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 18:26
 ***/
public class TestXmlManager {

	@BeforeClass
	public static void preExec() throws Throwable {
		ClassScanner.getInstance().scanner();
	}
	@Test
	public void test(){
		TestXmlService.instance.testReward();
		TestXmlService.instance.testSimpleMap();
		TestXmlService.instance.testNestMap();
		TestXmlService.instance.testNestList();
	}
}
