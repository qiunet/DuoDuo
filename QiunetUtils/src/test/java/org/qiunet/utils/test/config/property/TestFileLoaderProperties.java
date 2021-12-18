package org.qiunet.utils.test.config.property;

import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 * @author qiunet
 * 2020-04-25 08:10
 **/
public class TestFileLoaderProperties {

	public static void main(String[] args) throws Exception {
		ClassScanner.getInstance(ScannerType.FILE_CONFIG).scanner();
		for (int i = 0; i < 20; i++) {
			// 过程中, 自己去改变target下, db.properties content的内容. 会产生变化即可
			System.out.println(DbProperties.getContent());
			Thread.sleep(1000);
		}
	}
}
