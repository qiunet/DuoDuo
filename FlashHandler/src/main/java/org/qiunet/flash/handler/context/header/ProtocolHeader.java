package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
/**
 * 请求的固定头
 * Created by qiunet.
 * 17/7/19
 */
public class ProtocolHeader {
	/**包头识别码*/
	private static  final byte [] MAGIC_CONTENTS = {'f', 'a', 's', 't'};


	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 28;
	/**辨别 请求使用*/
	private byte [] magic;
	// 长度
	private int length;
	// 请求序列
	private int sequence;
	// 请求的 响应的协议 id
	private int protocolId;
	// byte 的加密块大小 (多个块互换)
	private int chunkSize;
	// crc code
	private long crc;

	/***
	 * 构造函数
	 * 不使用datainputstream了.  不确定外面使用的是什么.
	 * 由外面读取后 调构造函数传入
	 * @param length 后面byte数组 长度
	 * @param sequence 请求的序列码
	 * @param protocolId 请求的id
	 * @param chunkSize 加密块的大小
	 * @param crc crc 完整校验
	 */
	public ProtocolHeader(int length, int sequence, int protocolId, int chunkSize, long crc) {
		this.magic = MAGIC_CONTENTS;
		this.crc = crc;
		this.length = length;
		this.sequence = sequence;
		this.chunkSize = chunkSize;
		this.protocolId = protocolId;
	}

	public ProtocolHeader(){}


	public ProtocolHeader(ByteBuf in) {
		this.magic = new byte[MAGIC_CONTENTS.length];
		in.readBytes(magic);
		this.length = in.readInt();
		this.sequence = in. readInt();
		this.protocolId = in.readInt();
		this.chunkSize = in.readInt();
		this.crc = in.readLong();
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public long getCrc() {
		return crc;
	}

	public int getLength() {
		return length;
	}

	public int getSequence() {
		return sequence;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public  void writeToByteBuf(ByteBuf out) {
		out.writeBytes(magic);
		out.writeInt(length);
		out.writeInt(sequence);
		out.writeInt(protocolId);
		out.writeInt(chunkSize);
		out.writeLong(crc);
	}
}
