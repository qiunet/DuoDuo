package org.qiunet.utils.data;

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
	 * short 转换为byte数组
	 * @param value
	 * @return
	 */
	public static byte [] short2Bytes(int value) {
		byte [] bytes = new byte[2];
		write16bit(value, bytes, 0);
		return bytes;
	}

	/**
	 * Writes a 16bit integer at the index.
	 */
	public static void write16bit(int value, byte[] code, int index) {
		code[index] = (byte)(value >>> 8);
		code[index + 1] = (byte)value;
	}

	/**
	 * Reads a 32bit integer at the index.
	 */
	public static int read32bit(byte[] code, int index) {
		return (code[index] << 24) | ((code[index + 1] & 0xff) << 16)
			| ((code[index + 2] & 0xff) << 8) | (code[index + 3] & 0xff);
	}

	/**
	 * int 转换为byte数组
	 * @param value
	 * @return
	 */
	public static byte [] int2Bytes(int value) {
		byte [] bytes = new byte[4];
		write32bit(value, bytes, 0);
		return bytes;
	}

	/**
	 * Writes a 32bit integer at the index.
	 */
	public static void write32bit(int value, byte[] code, int index) {
		code[index] = (byte)(value >>> 24);
		code[index + 1] = (byte)(value >>> 16);
		code[index + 2] = (byte)(value >>> 8);
		code[index + 3] = (byte)value;
	}

}
