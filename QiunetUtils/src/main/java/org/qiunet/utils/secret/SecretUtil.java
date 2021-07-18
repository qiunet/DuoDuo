package org.qiunet.utils.secret;

/***
 * 加密工具类 内容处理方法集合
 *
 * qiunet
 * 2021/7/18 07:58
 **/
 final class SecretUtil {
	/**
	 * MD5加密
	 * @param source
	 * @return
	 */
	 static final char[] hexDigits={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	/**
	 * 字节数组转字符串
	 * @param b
	 * @return
	 */
	 static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for(int i=0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 字节转字符串
	 * @param b
	 * @return
	 */
	 static String byteToHexString(byte b) {
		int n=b;
		if(n < 0) {
			n=256 + n;
		}
		int d1=n / 16;
		int d2=n % 16;
		return hexDigits[d1] + String.valueOf(hexDigits[d2]);
	}
}
