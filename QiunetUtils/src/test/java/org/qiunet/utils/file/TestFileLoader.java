package org.qiunet.utils.file;

import org.junit.Ignore;

import java.io.File;
import java.io.IOException;

public class TestFileLoader {
	private File file = new File(System.getProperty("user.dir"), "Test.txt");

	private String content = "我是qiunet!";
	@Ignore
	public void testLoader() throws IOException, InterruptedException {
		if (! file.exists()) file.createNewFile();

		FileUtil.appendToFile(file, content);
		FileLoader.listener(file, file -> System.out.println("---------"+file.getName()));
		FileUtil.appendToFile(file, content);
		Thread.sleep(10000);

		FileUtil.deleteFile(file);
	}

}
