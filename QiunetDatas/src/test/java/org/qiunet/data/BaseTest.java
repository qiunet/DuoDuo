package org.qiunet.data;

import org.junit.BeforeClass;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 11:11
 */
public class BaseTest {
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}
}
