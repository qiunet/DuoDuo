package org.qiunet.utils.encryptAndDecrypt;

import java.util.zip.CRC32;

/**
 * Created by qiunet.
 * 17/11/24
 */
public class CrcUtil {
	/**
	 * 得到crc32计算的crc值
	 * @param bytes
	 * @return
	 */
	public static long getCrc32Value(byte [] bytes) {
		CRC32 crc32 = new CRC32();
		crc32.update(bytes);
		return crc32.getValue();
	}
}
