package org.qiunet.utils.test.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.io.IOException;
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
		Assertions.assertTrue(file.exists());
		file.delete();
		//
		FileUtil.move(targetFile, currPath);
		file = new File(currPathFile);
		Assertions.assertTrue(file.exists());

		FileUtil.move(file, targetFilePath);
		file = new File(targetFile);
		Assertions.assertTrue(file.exists());

		FileUtil.copy(targetFile, currPathFile);
		file = new File(currPathFile);
		Assertions.assertTrue(file.exists());
		file.delete();
	}
	@Test
	public void appendToFile() throws IOException {
		File file = new File(System.getProperty("user.dir"), "Test.txt");
		File file1 = new File(System.getProperty("user.dir"), "Test1.txt");
		FileUtil.deleteFile(file);
		for (int i = 0; i < 5; i++) {
			FileUtil.appendToFile(file, "中Ha"+i);
		}

		List<String> list = FileUtil.tailFile(file, 10);
		Assertions.assertEquals(5, list.size());
		for (int i = 0; i < list.size(); i++) {
			Assertions.assertEquals(list.get(i), "中Ha"+i);
		}

		long startPos = FileUtil.getFileLength(file);

		FileUtil.copy(file, file1.getAbsolutePath());
		Assertions.assertEquals(FileUtil.getFileContent(file1), "中Ha0\n" +
			"中Ha1\n" +
			"中Ha2\n" +
			"中Ha3\n" +
			"中Ha4\n");

		FileUtil.createFileWithContent(file1, FileUtil.getFileContent(file1));
		Assertions.assertEquals(FileUtil.getFileContent(file1), "中Ha0\n" +
			"中Ha1\n" +
			"中Ha2\n" +
			"中Ha3\n" +
			"中Ha4\n");

		FileUtil.deleteFile(file1);

		FileUtil.appendToFile(file, "中Ha5");

		list = FileUtil.tailFile(file, startPos, 10);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals("中Ha5", list.get(0));

		Assertions.assertTrue(file.exists());
		FileUtil.deleteFile(file);
        Assertions.assertFalse(file.exists());
	}
}
