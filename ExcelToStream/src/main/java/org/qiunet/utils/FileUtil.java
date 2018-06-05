package org.qiunet.utils;

import java.io.*;

public class FileUtil {
	private static final String loadWorkHomeName = ".xd.project";

	public static String getFileName(){
		return System.getProperty("user.dir") + File.separator + loadWorkHomeName;
	}
	/**
	 *
	 * @param path
	 */
	public static void writeToProjectFile(String path) {
		String writeToFileName = getFileName();

		System.out.println("WriteTo " + writeToFileName);
		PrintWriter print = null;
		try {
			print = new PrintWriter(new File(writeToFileName), "UTF-8");
			print.println(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (print != null) print.close();
		}
	}

	public static String returnPathFromProjectFile(){
		String fileName = getFileName();
		File file = new File(fileName);
		if (! file.exists()) return null;

		LineNumberReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(new File(fileName));
			InputStreamReader isr = new InputStreamReader(fis , "UTF-8");
			reader = new LineNumberReader(isr);
			return reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
			if (reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
