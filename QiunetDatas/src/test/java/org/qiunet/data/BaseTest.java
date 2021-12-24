package org.qiunet.data;

import org.junit.jupiter.api.BeforeAll;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 11:11
 */
public class BaseTest {
	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.FILE_CONFIG).scanner();
	}
}
