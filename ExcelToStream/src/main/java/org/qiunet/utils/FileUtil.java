package org.qiunet.utils;

import java.io.*;

public class FileUtil {

	/**
	 *
	 * @param path
	 */
	public static void writeToProjectFile(String path, String writeToFileName) {
		System.out.println("WriteTo " + writeToFileName);
		FileOutputStream fos = null;
		PrintWriter print = null;
		try {
			print = new PrintWriter(new File(writeToFileName), "UTF-8");
			print.println(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
				try {
					if (print != null) print.close();
					if (fos != null) fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static String returnPathFromProjectFile( String fileName){
		File file = new File(fileName);
		if (! file.exists()) return null;

		LineNumberReader reader = null;
		FileReader fileReader = null;
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
			if (fileReader != null)fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
