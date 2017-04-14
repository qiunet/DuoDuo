package org.qiunet.handler.iodata.base;

/**
 * @author qiunet
 *         Created on 17/3/1 20:19.
 */
public interface OutputByteStream {
	/**
	 * 写入一个byte
	 * @param val
	 */
	public void writeByte(String desc, byte val) throws Exception;

	/**
	 * 写入一个short
	 * @param val
	 */
	public void writeShort(String desc, short val) throws Exception;
	/**
	 * 写入一个 int
	 * @param val
	 */
	public void writeInt(String desc, int val) throws Exception;
	/**
	 * 写入一个 long
	 * @param val
	 */
	public void writeLong(String desc, long val) throws Exception;
	/**
	 * 写入一个 float
	 * @param val
	 */
	public void writeFloat(String desc, float val) throws Exception;
	/**
	 * 写入一个 double
	 * @param val
	 */
	public void writeDouble(String desc, double val) throws Exception;
	/**
	 * 写入一个 boolean 值
	 * @param bool
	 */
	public void writeBoolean(String desc, boolean bool) throws Exception;
	/**
	 * 写入一个string 
	 * @param string
	 */
	public void writeString(String desc, String string) throws Exception;
	/**
	 * 写入一个byte数组
	 * @param bytes
	 * @throws Exception
	 */
	public void writeBytes(byte[] bytes)throws Exception;

	/**
	 * 得到这个OutputByteStream 的byte数组
	 * @throws Exception
	 */
	public byte [] getBytes()throws Exception;
	/**
	 * 关闭
	 */
	public void close()throws Exception;
}
