package org.qiunet.utils.file;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	/****
	 * 读取文件的最后{lastNum}行.
	 * @param file
	 * @param lastNum
	 * @return
	 */
	public static List<String> tailFile(File file, int lastNum) {
		List<String> result = new ArrayList<>();
		if (file == null || lastNum <= 0 || file.isDirectory() || ! file.exists() || !file.canRead()) {
			return result;
		}

		RandomAccessFile reader = null;
		try {
			reader = new RandomAccessFile(file, "r");
			long length = reader.length();
			if (length <= 0) {
				return result;
			}

			int count = 0;
			long pos = length - 1;
			while (pos > 0) {
				pos--;
				reader.seek(pos);
				if (reader.readByte() == '\n') {
					String line = reader.readLine();
					result.add(line);

					count ++;
					if (count >= lastNum) {
						break;
					}
				}

				if (pos == 0) {
					reader.seek(0);
					result.add(reader.readLine());
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		if (! result.isEmpty()) {
			Collections.reverse(result);
		}
		return result;
	}
	private FileUtil(){}


	/**
	 * 删除文件夹
	 * folderPath文件夹完整绝对路径
	 *
	 * @param
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * path文件夹完整绝对路径
	 *
	 * @param
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 删除文件
	 *
	 * @param filepath
	 * @throws IOException
	 */
	public static void del(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
		}
	}

	/**
	 * 读取文件内容
	 *
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getFileContent(String fileName) throws IOException {
		String content = "";
		File versionFile = new File(fileName);
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		String str = "";
		try {
			in = new BufferedReader(new FileReader(versionFile));
			while ((str = in.readLine()) != null) {
				if(sb.length() > 0){
					sb.append("\n");
				}
				sb.append(str);
			}
			content = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) in.close();
		}
		return content;
	}

	/**
	 * 数据流-读二进制文件
	 *
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static DataInputStream getDataInputStream(String fileName) throws Exception {
		InputStream in = null;
//        ByteArrayInputStream bin = null;
		DataInputStream dis = null;
		try {
			in = new FileInputStream(fileName);
//            bin=new ByteArrayInputStream(in);
			dis = new DataInputStream(in);
			return dis;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取版本文件:" + fileName + "失败.");
			if (in != null) {
				in.close();
			}
		}
		return null;
	}
}
