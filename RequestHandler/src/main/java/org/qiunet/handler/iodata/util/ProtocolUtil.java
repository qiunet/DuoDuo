package org.qiunet.handler.iodata.util;
/**
 * @author Zero
 * @mail baozilaji@126.com
 * @datetime May 26, 2015 10:53:39 PM
 * @desc 协议加密解密工具
 */
public class ProtocolUtil {
	/**
	 * byte 加密
	 * @param data
	 * @param chunkSize
	 * @return
	 */
	public static byte[] encryptData(byte[] data, int chunkSize) {
		if (data == null) {
			return null;
		}
		
		int len = data.length;
		byte[] ret = new byte[len];
		if (chunkSize <= 0 || chunkSize >= len) {
			System.arraycopy(data, 0, ret, 0, len);
			return ret;
		}
		
		int chunkNum = len / chunkSize;
		int oddSize = len % chunkSize;
		int srcPos = len;
		int destPos = 0;
		
		if (oddSize != 0) {
			srcPos -= oddSize;
			System.arraycopy(data, srcPos, ret, destPos, oddSize);
			destPos += oddSize;
		}
		for (int i = chunkNum - 1; i >= 0; i--) {
			srcPos -= chunkSize;
			System.arraycopy(data, srcPos, ret, destPos, chunkSize);
			destPos += chunkSize;
		}
		return ret;
	}
	/**
	 * byte 解密
	 * @param data
	 * @param chunkSize
	 * @return
	 */
	public static byte[] decryptData(byte[] data, int chunkSize) {
		if (data == null) {
			return null;
		}
		
		int len = data.length;
		byte[] ret = new byte[len];
		if (chunkSize <= 0 || chunkSize >= len) {
			System.arraycopy(data, 0, ret, 0, len);
			return ret;
		}
		
		int chunkNum = len / chunkSize;
		int oddSize = len % chunkSize;
		int srcPos = 0;
		int destPos = len;
		
		if (oddSize != 0) {
			destPos -= oddSize;
			System.arraycopy(data, srcPos, ret, destPos, oddSize);
			srcPos += oddSize;
		}
		for (int i = chunkNum - 1; i >= 0; i--) {
			destPos -= chunkSize;
			System.arraycopy(data, srcPos, ret, destPos, chunkSize);
			srcPos += chunkSize;
		}
		return ret;
	}
}
