package org.qiunet.utils.file;

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
		if (path.contains("\\")) {
			return path.substring(path.lastIndexOf("\\") + 1);
		}
		return path.substring(path.lastIndexOf("/") + 1);
	}

	/**
	 * 获得文件夹路径
	 *  比如: /Users/qiunet/doc/x.txt
	 * 	得到  /Users/qiunet/doc
	 * @param path
	 * @return
	 */
	public static String dirName(String path) {
		if (path.contains("\\")) {
			return path.substring(0, path.lastIndexOf("\\"));
		}
		return path.substring(0, path.lastIndexOf("/"));
	}
}
