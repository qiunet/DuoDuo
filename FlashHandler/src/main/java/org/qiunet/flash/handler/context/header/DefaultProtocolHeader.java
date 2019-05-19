package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;

import java.util.Arrays;

/**
 * 请求的固定头
 * Created by qiunet.
 * 17/7/19
 */
public class DefaultProtocolHeader implements IProtocolHeader {
	/**包头识别码*/
	private static  final byte [] MAGIC_CONTENTS = {'f', 'a', 's', 't'};


	/**请求头固定长度*/
	private static final int REQUEST_HEADER_LENGTH = 16;
	/**辨别 请求使用*/
	private byte [] magic;
	// 长度
	private int length;
	// 请求的 响应的协议 id
	private int protocolId;
	// encryption code
	private int crc;

	/***
	 * 构造函数
	 * 不使用datainputstream了.  不确定外面使用的是什么.
	 * 由外面读取后 调构造函数传入
	 * @param bytes 后面byte数组
	 * @param protocolId 请求的id
	 */
	public DefaultProtocolHeader(byte [] bytes, int protocolId) {
		this.magic = MAGIC_CONTENTS;
		this.crc = (int) CrcUtil.getCrc32Value(bytes);
		this.length = bytes.length;
		this.protocolId = protocolId;
	}

	public DefaultProtocolHeader() {
	}


	@Override
	public IProtocolHeader parseHeader(ByteBuf in) {
		this.magic = new byte[MAGIC_CONTENTS.length];
		in.readBytes(magic);
		this.length = in.readInt();
		this.protocolId = in.readInt();
		this.crc = in.readInt();
		return this;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public boolean encryptionValid(Object validData) {
		return ((Long) validData).intValue() == this.crc;
	}

	@Override
	public int getHeaderLength() {
		return REQUEST_HEADER_LENGTH;
	}

	@Override
	public int getProtocolId() {
		return protocolId;
	}

	@Override
	public boolean isMagicValid(){
		return Arrays.equals(this.magic, MAGIC_CONTENTS);
	}

	@Override
	public  void writeToByteBuf(ByteBuf out) {
		out.writeBytes(magic);
		out.writeInt(length);
		out.writeInt(protocolId);
		out.writeInt(crc);
	}

	@Override
	public String toString() {
		return "DefaultProtocolHeader{" +
				"magic=" + Arrays.toString(magic) +
				", length=" + length +
				", protocolId=" + protocolId +
				", encryption=" + crc +
				'}';
	}
}
