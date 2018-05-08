package org.qiunet.utils.file;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author qiunet
 *         Created on 17/2/17 18:02.
 */
public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
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
		writeStringToFile(new File(filePath), msg, "UTF-8", append);
	}

	/***
	 * 写入数据到文件
	 * @param file 文件
	 * @param data 数据
	 * @param encoding 编码
	 * @param append 是否append
	 */
	public static void writeStringToFile(final File file, final String data, final String encoding, final boolean append){
		Charset charset = Charset.forName(encoding);

		if (file.exists()) {
			if (file.isDirectory()) {
				logger.error("File '" + file + "' exists but is a directory");
				return;
			}
			if (file.canWrite() == false) {
				file.setWritable(true);
			}
		} else {
			final File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					logger.error("Directory '" + parent + "' could not be created");
					return;
				}
			}
		}
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file, append);
			output.write(data.getBytes(charset));
		} catch (Exception e) {
			logger.error("FileUtil Exception", e);
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					logger.error("FileUtil Exception", e);
				}
		}
	}
	private FileUtil(){}
}
