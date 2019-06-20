package org.qiunet.utils.file;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author qiunet
 *         Created on 17/2/17 18:02.
 */
public class FileUtil {
	private static Logger logger = LoggerType.DUODUO.getLogger();
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
		return srcFile.renameTo(new File(dir, srcFile.getName()));
	}

	/**
	 * 移动文件  只能mv到文件夹
	 * @param srcFile 文件
	 * @param destPath 文件夹路径
	 * @return 成功与否
	 */
	public static boolean move(String srcFile, String destPath) {
		// File (or directory) to be moved
		return move(new File(srcFile), destPath);
	}

	/**
	 * copy文件
	 * @param oldPath
	 * @param newPath
	 */
	public static void copy(String oldPath, String newPath) {
		copy(new File(oldPath), newPath);
	}

	/**
	 * copy文件
	 * @param oldFile
	 * @param newPath
	 */
	public static void copy(File oldFile, String newPath) {
		if (oldFile.exists()) {
			int byteRead;
			try (InputStream inStream = new FileInputStream(oldFile);
				 FileOutputStream fs = new FileOutputStream(newPath)) {
				byte[] buffer = new byte[1444];
				while ((byteRead = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteRead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 写东西到文件去
	 * @param file
	 * @param msg
	 */
	public static void appendToFile(File file, String msg){
		writeStringToFile(file, msg, StandardCharsets.UTF_8, true, "\n");
	}

	/***
	 * 写入数据到文件
	 * @param file 文件
	 * @param data 数据
	 * @param charset 编码
	 * @param append 是否append
	 */
	public static void writeStringToFile(final File file, final String data, final Charset charset, final boolean append, String endChar){
		if (file.exists()) {
			if (file.isDirectory()) {
				logger.error("File '" + file + "' exists but is a directory");
				return;
			}
		} else {
			final File parent = file.getParentFile();
			if (!parent.mkdirs() && !parent.isDirectory()) {
				logger.error("Directory '" + parent + "' could not be created");
				return;
			}
		}

		try (FileOutputStream output = new FileOutputStream(file, append)){
			output.write((data+endChar).getBytes(charset));
		} catch (Exception e) {
			logger.error("FileUtil Exception", e);
		}
	}

	/****
	 * 读取文件的最后{lastNum}行.
	 * @param file
	 * @param lastNum
	 * @return
	 */
	public static List<String> tailFile(File file, int lastNum) {
		return tailFile(file, 0, lastNum);
	}
	/****
	 * 从某个位置开始读取文件的最后{lastNum}行.
	 * @param file
	 * @param lastNum
	 * @return
	 */
	public static List<String> tailFile(File file, long startPos, int lastNum) {
		if (startPos > 0) startPos--;
		List<String> result = new ArrayList<>();
		if (file == null || lastNum <= 0 || file.isDirectory() || ! file.exists() || !file.canRead()) {
			return result;
		}

		try (RandomAccessFile reader = new RandomAccessFile(file, "r")) {
			long length = reader.length();
			if (length <= 0) return result;

			int count = 0;
			long pos = length - 1;
			while (pos-- > startPos) {
				reader.seek(pos);
				if (reader.readByte() == '\n') {
					result.add(StandardCharsets.UTF_8.decode(StandardCharsets.ISO_8859_1.encode(reader.readLine())).toString());
					if (++count >= lastNum) break;
				}

				if (pos == 0) {
					reader.seek(0);
					result.add(StandardCharsets.UTF_8.decode(StandardCharsets.ISO_8859_1.encode(reader.readLine())).toString());
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}

		if (! result.isEmpty()) {
			Collections.reverse(result);
		}
		return result;
	}

	/***
	 * 得到文件长度
	 * 一般配合tail 用作startPos
	 * 判断是否文件长度有变化.
	 * @param file
	 * @return
	 */
	public static long getFileLength(File file) {
		if (file == null || file.isDirectory() || !file.exists() || !file.canRead()) {
			return 0;
		}
		try (RandomAccessFile reader = new RandomAccessFile(file, "r")) {
			return reader.length();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private FileUtil(){}
	/**
	 * 删除文件 或者文件夹 以及其子目录下所有文件
	 *
	 * @param f
	 * @throws IOException
	 */
	public static void delAllFile(File f) {
		if (! f.exists()) return;

		if (f.isFile()) {
			f.delete();
			return;
		}

		File [] files = f.listFiles();
		if(files == null) return;

		for (File file : files) {
			delAllFile(file);
		}
		f.delete();
	}

	/**
	 * 读取文件内容
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileContent(File file) throws IOException {
		if (! file.exists()) return null;

		byte[] bytes = Files.readAllBytes(file.toPath());
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
