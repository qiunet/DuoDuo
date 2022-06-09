package org.qiunet.utils.data;

import java.nio.ByteBuffer;

/***
 *
 *
 * @author qiunet
 * 2020-11-17 12:48
 */
public class ByteUtil {

	/**
	 * Reads an unsigned 16bit integer at the index.
	 */
	public static int readU16bit(byte[] code, int index) {
		return ((code[index] & 0xff) << 8) | (code[index + 1] & 0xff);
	}

	/**
	 * Reads a signed 16bit integer at the index.
	 */
	public static int readS16bit(byte[] code, int index) {
		return (code[index] << 8) | (code[index + 1] & 0xff);
	}




	/**
	 * Reads a 32bit integer at the index.
	 */
	public static int read32bit(byte[] code, int index) {
		return (code[index] << 24) | ((code[index + 1] & 0xff) << 16)
			| ((code[index + 2] & 0xff) << 8) | (code[index + 3] & 0xff);
	}
	/**
	 * short 转换为byte数组
	 * @param value
	 * @return
	 */
	public static byte [] short2Bytes(int value) {
		return ByteBuffer.allocate(2).putShort((short) value).array();
	}

	/**
	 * int 转换为byte数组
	 * @param value
	 * @return
	 */
	public static byte [] int2Bytes(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	/**
	 * long 转换为byte数组
	 * @param value
	 * @return
	 */
	public static byte [] long2Bytes(long value) {
		return ByteBuffer.allocate(8).putLong(value).array();
	}

	/**
	 * 读取ByteBuffer的内容. 返回array
	 * @param buffer
	 * @return
	 */
	public static byte [] readBytebuffer(ByteBuffer buffer) {
		if (buffer.hasArray()) {
			return buffer.array();
		}
		return readBytebuffer(buffer, 0, buffer.limit());
	}
	/**
	 * 读取ByteBuffer的内容. 返回array
	 * @param buffer
	 * @return
	 */
	public static byte [] readBytebuffer(ByteBuffer buffer, int index, int length) {
		byte [] bytes = new byte[length];
		buffer.get(bytes, index, length);
		return bytes;
	}
}
