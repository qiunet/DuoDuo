package org.qiunet.utils.file;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
	@Test
	public void appendToFile() throws IOException {
		File file = new File(System.getProperty("user.dir"), "Test.txt");
		for (int i = 0; i < 5; i++) {
			FileUtil.writeStringToFile(file, "中Ha"+i, StandardCharsets.UTF_8, true, "\n");
		}

		List<String> list = FileUtil.tailFile(file, 10);
		Assert.assertEquals(5, list.size());
		for (int i = 0; i < list.size(); i++) {
			Assert.assertEquals(list.get(i), "中Ha"+i);
		}

		Assert.assertEquals(FileUtil.getFileContent(file), "中Ha0\n" +
																	"中Ha1\n" +
																	"中Ha2\n" +
																	"中Ha3\n" +
																	"中Ha4\n");

		Assert.assertTrue(file.exists());
		FileUtil.delAllFile(file);
		Assert.assertTrue(! file.exists());
	}
}
