package org.qiunet.utils.file;

import java.io.*;

/**
 * @author qiunet
 *         Created on 17/2/17 18:02.
 */
public class FileUtil {
	/**
	 * 移动文件
	 * @param srcFile
	 * @param destPath
	 * @return
	 */
	public static boolean move(File srcFile, String destPath) {
		// Destination directory
		File dir = new File(destPath);

		// move file to new directory
		boolean success = srcFile.renameTo(new File(dir, srcFile.getName()));

		return success;
	}

	/**
	 * 移动文件  只能mv到文件夹
	 * @param srcFile 文件
	 * @param destPath 文件夹路径
	 * @return 成功与否
	 */
	public static boolean move(String srcFile, String destPath) {
		// File (or directory) to be moved
		File file = new File(srcFile);

		// Destination directory
		File dir = new File(destPath);

		// move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));

		return success;
	}

	/**
	 * copy文件
	 * @param oldPath
	 * @param newPath
	 */
	public static void copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * copy文件
	 * @param oldfile
	 * @param newPath
	 */
	public static void copy(File oldfile, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			//File     oldfile     =     new     File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 写东西到文件去
	 * @param filePath
	 * @param msg
	 */
	public static void appendToFile(String filePath, String msg , boolean append){
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(filePath, append);
			bw = new BufferedWriter(fw);
			bw.write(msg);
			bw.newLine();
			bw.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(bw != null )bw.close();
				if(fw != null )fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private FileUtil(){}
}
