package org.qiunet.utils.test.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.file.DPath;

/***
 *
 * @author qiunet
 * 2022/1/24 13:50
 */
public class TestDPath {
	@Test
	public void testUnixDirName() {
		Assertions.assertEquals("/Users/qiunet", DPath.dirName("/Users/qiunet/ss.txt"));
	}
	@Test
	public void testUnixFileName() {
		Assertions.assertEquals("ss.txt", DPath.fileName("/Users/qiunet/ss.txt"));
	}

	@Test
	public void testWinDirName() {
		Assertions.assertEquals("D:\\WorkSpace", DPath.dirName("D:\\WorkSpace\\ss.txt"));
	}

	@Test
	public void testWinFileName() {
		Assertions.assertEquals("ss.txt", DPath.fileName("D:\\WorkSpace\\ss.txt"));
	}
}
