package org.qiunet.utils.secret;

import java.nio.ByteBuffer;
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

	/**
	 * 得到crc32计算的crc值
	 * @param byteBuffer
	 * @return
	 */
	public static long getCrc32Value(ByteBuffer byteBuffer) {
		CRC32 crc32 = new CRC32();
		byteBuffer.mark();
		crc32.update(byteBuffer);
		byteBuffer.reset();
		return crc32.getValue();
	}
}
