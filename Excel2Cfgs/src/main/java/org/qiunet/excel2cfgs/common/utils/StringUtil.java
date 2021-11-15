package org.qiunet.excel2cfgs.common.utils;

import java.nio.charset.StandardCharsets;

/***
 *
 * @author qiunet
 * 2021/11/15 14:07
 */
public class StringUtil {

	public static boolean isEmpty(String val) {
		return val == null || "".equals(val.trim());
	}

	/***
	 * 是不是字符串
	 * @param numStr
	 * @return
	 */
	public static boolean isNum(String numStr) {
		if (numStr == null) return false;
		return numStr.matches("-?[0-9]+");
	}

	/**
	 * 加密  腾讯安全sdk 的strData 编码就这个
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		if (data == null)
			return "";
		StringBuilder encryptData = new StringBuilder(128);
		byte [] bytes = data.getBytes(StandardCharsets.UTF_8);
		for(byte b : bytes){
			encryptData.append(String.format("%02x", b));
		}
		return encryptData.toString();
	}

	/***
	 * 解密
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {
		byte [] bytes = new byte[(int)Math.ceil(data.length()/2f)];

		int i = 0 ,len = data.length();
		while (i < len) {
			int segementLen = 2;
			if(len - i == 1) segementLen = 1;
			String subStr = data.substring(i ,i+segementLen);
			short s = Short.valueOf(subStr, 16);
			bytes[i/2] = ((byte)s);
			i += segementLen;
		}

		return new String(bytes, StandardCharsets.UTF_8);
	}
}
