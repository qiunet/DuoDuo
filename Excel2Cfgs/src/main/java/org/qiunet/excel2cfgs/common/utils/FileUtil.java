package org.qiunet.excel2cfgs.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/***
 *
 * @author qiunet
 * 2021/11/15 14:01
 */
public class FileUtil {
	public static void createFileWithContent(File file, String data){
		try (
				FileOutputStream output = new FileOutputStream(file, false)){
			output.write((data).getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void appendToFile(File file, String append) {
		try (FileOutputStream output = new FileOutputStream(file, true)){
			output.write((append+"\n").getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFileContent(File file) throws IOException {
		byte[] bytes = Files.readAllBytes(file.toPath());
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
