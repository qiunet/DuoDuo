package org.qiunet.utils.property;

/***
 *
 * @author qiunet
 * 2020-04-25 08:10
 **/
public class TestFileLoaderProperties {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			String content = DbProperties.getInstance().getString("content");
			// 过程中, 自己去改变target下, db.properties content的内容. 会产生变化即可
			System.out.println(content);
			Thread.sleep(1500);
		}
	}
}
