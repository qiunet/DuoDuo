package org.qiunet.function.test.attr;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.function.attr.manager.AttrManager;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 16:59
 */
public class AttrTest {

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void test(){
		AttrManager.printAttrTree();
	}
}
