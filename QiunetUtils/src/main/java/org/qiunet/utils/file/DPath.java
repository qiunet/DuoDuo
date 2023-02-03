package org.qiunet.utils.file;

import org.qiunet.utils.exceptions.CustomException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Predicate;

/***
 *
 * @author qiunet
 * 2022/1/24 13:46
 */
public class DPath {
	/**
	 * 从path获得文件名.
	 * 比如: /Users/qiunet/doc/x.txt
	 * 得到x.txt
	 * @param path 路径
	 * @return 文件名
	 */
	public static String fileName(String path) {
		return Paths.get(path).getFileName().toString();
	}

	/**
	 * 获得文件夹路径
	 *  比如: /Users/qiunet/doc/x.txt
	 * 	得到  /Users/qiunet/doc
	 * @param path
	 * @return
	 */
	public static String dirName(String path) {
		Path path0 = Paths.get(path);
		if (path0.toFile().isDirectory()) {
			return path0.toString();
		}
		return path0.getParent().toString();
	}

	/**
	 * 返回文件夹下面的文件数组
	 * @param dirPath
	 * @return
	 */
	public static void listDir(String dirPath, Consumer<File> consumer, Predicate<File> filter) {
		File dir = new File(dirPath);
		if (! dir.isDirectory()) {
			throw new CustomException("{} not a directory", dirPath);
		}
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (filter != null && ! filter.test(file)) {
				continue;
			}
			consumer.accept(file);
		}
	}
}
