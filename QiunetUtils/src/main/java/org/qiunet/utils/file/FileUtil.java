package org.qiunet.utils.file;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FileUtils;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

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
		// srcFile File (or directory) to be moved
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
		if (! oldFile.exists() || !oldFile.isFile()) throw new IllegalArgumentException("file ["+oldFile.getAbsolutePath()+"] is not exist or is not a file!");

		try (FileOutputStream fs = new FileOutputStream(newPath)) {
			fs.write(Files.readAllBytes(oldFile.toPath()));
		} catch (Exception e) {
			logger.error("Exception" , e);
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
	 * 使用content构造一个新文件
	 * 如果已经有该文件. 将被覆盖.
	 * @param file
	 * @param content
	 */
	public static void createFileWithContent(File file, String content){
		writeStringToFile(file, content, StandardCharsets.UTF_8, false, "");
	}
	/***
	 * 写入数据到文件
	 * @param file 文件
	 * @param data 数据
	 * @param charset 编码
	 * @param append 是否append
	 */
	public static void writeStringToFile(final File file, final String data, final Charset charset, final boolean append, String endChar){
		if (file.isDirectory()) {
			logger.error("File '" + file + "' exists but is a directory");
			return;
		}
		if (! file.exists() && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			logger.error("Directory '" + file.getParent() + "' could not be created");
			return;
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
		long length, pos;
		if ((length = getFileLength(file)) <= 0 || startPos < 0) {
			return result;
		}
		pos = length - 1;
		int count = 0;
		try (RandomAccessFile reader = new RandomAccessFile(file, "r")) {
			while (pos-- > startPos) {
				reader.seek(pos);
				if (pos == 0 || reader.readByte() == '\n') {
					result.add(StandardCharsets.UTF_8.decode(StandardCharsets.ISO_8859_1.encode(reader.readLine())).toString());
					if (++count >= lastNum) break;
				}
			}
		}catch (IOException e) {
			logger.error("Exception" , e);
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
		return file.length();
	}

	private FileUtil(){}
	/**
	 * 删除文件 或者文件夹 以及其子目录下所有文件
	 *
	 * @param f
	 * @throws IOException
	 */
	public static void deleteFile(File f) {
		if (! f.exists()) return;

		if (f.isFile()) {
			f.delete();
			return;
		}

		File [] files = f.listFiles();
		if(files == null) return;

		for (File file : files) {
			deleteFile(file);
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
		if (! file.exists() || !file.isFile()) return null;

		byte[] bytes = Files.readAllBytes(file.toPath());
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static void listFile(Path path, List<File> retList, Predicate<File> predicate) {
		Preconditions.checkNotNull(retList);

		File file = path.toFile();
		if (! file.exists()) return;

		File[] files = file.listFiles();
		if (files == null) return;

		for (File file2 : files) {
			if (file2.isFile() && predicate.test(file2)) {
				retList.add(file2);
				continue;
			}

			if (file2.isDirectory()) {
				listFile(file2.toPath(), retList, predicate);
			}
		}
	}

	/**
	 * 清理文件夹
	 * @param directory
	 * @throws IOException
	 */
	public static void cleanDirectory(final File directory) throws IOException {
		FileUtils.cleanDirectory(directory);
	}

	/**
	 * 变动监听
	 * @param path
	 * @param changeCallback
	 */
	public static void changeListener(Path path, IFileChangeCallback changeCallback) {
		changeListener(path.toFile(), changeCallback);
	}
	/**
	 * 变动监听
	 * @param file
	 * @param changeCallback
	 */
	public static void changeListener(File file, IFileChangeCallback changeCallback) {
		FileChangeListener.listener(file, changeCallback);
	}
}
