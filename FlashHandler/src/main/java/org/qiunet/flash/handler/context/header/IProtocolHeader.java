package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;

/**
 * 一个协议头的接口. 考虑以后可以由启动类指定.
 *
 *
 * 一般使用默认DefaultProtocolHeader
 * .
 */
public interface IProtocolHeader {
	/**
	 * 得到协议ID
	 * @return
	 */
	int getProtocolId();
	/***
	 * 将header对象的内容输出到ByteBuf
	 * @param out
	 */
	void writeToByteBuf(ByteBuf out);
	/**
	 * 魔数是否是有效的.
	 * @return
	 */
	boolean isMagicValid();
	/**
	 * 得到body的长度
	 * @return
	 */
	int getLength();
	/***
	 * 对剩余bytes的校验数据进行校验
	 * @param validData
	 * @return
	 */
	boolean encryptionValid(Object validData);
}
