package org.qiunet.utils.file;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author qiunet
 *         Created on 17/2/17 18:02.
 */
public class FileUtil {
	private static final Logger logger = LoggerType.DUODUO.getLogger();
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
			throw new CustomException(e, "File Copy exception!");
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
			logger.error("File '{}' exists but is a directory", file);
			return;
		}
		if (! file.exists() && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			logger.error("Directory '{}' could not be created", file.getParent());
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
		if (file == null || file.isDirectory() || !file.exists()) {
			throw new CustomException("File is empty or null");
		}

		if (!file.canRead()) {
			return 0;
		}
		return file.length();
	}

	private FileUtil(){}
	/**
	 * 删除文件 或者文件夹 以及其子目录下所有文件
	 *
	 * @param file
	 * @throws IOException
	 */
	public static void deleteFile(File file) throws IOException {
		if (file == null || ! file.exists()) return;

		if (file.isFile()) {
			if (! file.delete()) {
				throw new IOException("Unable to delete file: " + file);
			}
			return;
		}

		File [] files = file.listFiles();
		if(files == null) return;

		for (File file0 : files) {
			deleteFile(file0);
		}
		// delete self
		if (! file.delete()) {
			throw new IOException("Unable to delete directory: " + file);
		}
	}

	/**
	 * 读取文件内容
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileContent(File file) throws IOException {
		if (file == null) {
			throw new NullPointerException();
		}

		if (! file.exists() || !file.isFile()) {
			throw new CustomException("File {} is not file or empty!", file.getAbsolutePath());
		}

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
		if (! directory.isDirectory()) {
			return;
		}

		final File[] files =directory.listFiles();
		if (files == null) {
			return;
		}

		IOException exception = null;
		for (final File file : files) {
			try {
				deleteFile(file);
			} catch (final IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
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
	/**
	 * 一行行的读取文件
	 * @param file 文件
	 * @param lineConsumer 消费者
	 */
	public static void readFileLines(File file, Consumer<String> lineConsumer) {
		String line;
		try (FileReader fileReader = new FileReader(file);
			 BufferedReader reader = new BufferedReader(fileReader)) {
			while ((line = reader.readLine()) != null) {
				lineConsumer.accept(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 一行行的读取文件
	 * @param file 文件
	 */
	public static List<String> readFileLines(File file) {
		String line;
		List<String> list = Lists.newLinkedList();
		try (FileReader fileReader = new FileReader(file);
			 BufferedReader reader = new BufferedReader(fileReader)) {
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
}
