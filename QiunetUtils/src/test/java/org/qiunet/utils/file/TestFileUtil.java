package org.qiunet.utils.file;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author qiunet
 *         Created on 17/2/17 18:05.
 */
public class TestFileUtil {
	@Test
	public void testCopy(){
		String baseDir = System.getProperty("user.dir");
		if (!baseDir.endsWith(File.separator)) baseDir += File.separator;

		String targetFilePath = baseDir + "clazzes/org/test/";
		String targetFile = targetFilePath + "ObjectB.class";

		String currPath = getClass().getResource(".").getPath();
		String currPathFile = currPath + "ObjectB.class";

		FileUtil.copy(targetFile, currPathFile);

		File file = new File(currPathFile);
		Assert.assertTrue(file.exists());
		file.delete();
		//
		FileUtil.move(targetFile, currPath);
		file = new File(currPathFile);
		Assert.assertTrue(file.exists());

		FileUtil.move(file, targetFilePath);
		file = new File(targetFile);
		Assert.assertTrue(file.exists());

		FileUtil.copy(targetFile, currPathFile);
		file = new File(currPathFile);
		Assert.assertTrue(file.exists());
		file.delete();
	}
	@Ignore
	public void appendToFile() throws IOException {
		FileUtil.writeStringToFile(new File("/Users/qiunet/desktop/xxx.txt"), "haha", "Utf-8", true);
	}
}
