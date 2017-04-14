package org.qiunet.handler.iodata.base;

/**
 * * 对流的操作. 
 * 可能有好几种
 * DataInputStream 
 * IoBuffer等
 * @author qiunet
 *         Created on 17/3/1 18:25.
 */
public interface InputByteStream {
	/**
	 * 对流的操作 读取一个byte 2个字节. 
	 * @return
	 */
	public byte readByte(String desc) throws Exception;

	/**
	 * 对流的操作 读取一个short 2个字节.
	 * @return
	 */
	public short readShort(String desc) throws Exception;
	/**
	 * 对流的操作 读取一个int 4个字节.
	 * @return
	 */
	public int readInt(String desc) throws Exception;
	/**
	 * 对流的操作 读取一个long 8个字节.
	 * @return
	 */
	public long readLong(String desc) throws Exception;
	/**
	 * 对流的操作 读取一个float 4个字节.
	 * @return
	 */
	public float readFloat(String desc) throws Exception;
	/**
	 * 对流的操作 读取一个double 8个字节.
	 * @return
	 */
	public double readDouble(String desc) throws Exception;
	/**
	 * 对流的操作 读取一个boolean 1个字节 也可以理解为byte 1 为true  other false.
	 * @return
	 */
	public boolean readBoolean(String desc) throws Exception;
	/**
	 * 对流的操作 读取一个字符串, 先读取一个short 2个字节为长度, 再读取剩余的byte[] 构造一个字符串.
	 * @return
	 */
	public String readString(String desc) throws Exception;

	/**
	 * 读取一定数量的byte
	 * @param length 长度
	 * @return byte 数组
	 * @throws Exception
	 */
	public byte[] readBytes(int length)throws Exception;
	/**
	 * 关闭
	 */
	public void close()throws Exception;
}
