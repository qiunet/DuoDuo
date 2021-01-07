package org.qiunet.utils.config.property;


import org.qiunet.utils.classScanner.ClassScanner;

/***
 *
 * @author qiunet
 * 2020-04-25 08:10
 **/
public class TestFileLoaderProperties {

	public static void main(String[] args) throws Exception {
		ClassScanner.getInstance().scanner();
		for (int i = 0; i < 20; i++) {
			// 过程中, 自己去改变target下, db.properties content的内容. 会产生变化即可
			System.out.println(DbProperties.getContent());
			Thread.sleep(1000);
		}
	}
}
