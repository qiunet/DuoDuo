package org.qiunet.utils.secret;

import java.nio.charset.StandardCharsets;

/**
 * 对字符串进行编码解码
 * 一般用于昵称 字符串存储多有不便 的地方
 * @author qiunet
 *         Created on 16/12/30 07:17.
 */
public class StrCodecUtil {
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
	
	private StrCodecUtil(){}
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
