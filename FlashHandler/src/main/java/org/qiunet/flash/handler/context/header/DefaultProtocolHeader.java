package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.secret.CrcUtil;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 请求的固定头
 * Created by qiunet.
 * 17/7/19
 */
public class DefaultProtocolHeader implements IProtocolHeader {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**包头识别码*/
	private static  final byte [] MAGIC_CONTENTS = {'f', 'a', 's', 't'};


	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 16;
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
	 * @param content 后面byte数组
	 */
	public DefaultProtocolHeader(MessageContent content) {
		this.magic = MAGIC_CONTENTS;
		this.crc = (int) CrcUtil.getCrc32Value(content.bytes());
		this.length = content.bytes().length;
		this.protocolId = content.getProtocolId();
	}

	public DefaultProtocolHeader(ByteBuf in) {
		this.magic = new byte[MAGIC_CONTENTS.length];
		in.readBytes(magic);
		this.length = in.readInt();
		this.protocolId = in.readInt();
		this.crc = in.readInt();
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public byte[] validAndDecryptBytes(byte [] bytes) {
		boolean ret = (int)CrcUtil.getCrc32Value(bytes) == this.crc;
		if (! ret) {
			logger.error("Invalid message encryption! server is : "+ CrcUtil.getCrc32Value(bytes) +" client is "+ this.crc);
		}
		// 仅仅使用crc32校验了, 没有进行加密
		return bytes;
	}

	@Override
	public byte[] encodeBytes(byte[] bytes) {
		return bytes;
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
	public  byte[] dataBytes() {
		ByteBuffer out = ByteBuffer.allocate(REQUEST_HEADER_LENGTH);
		out.put(magic);
		out.putInt(length);
		out.putInt(protocolId);
		out.putInt(crc);
		return out.array();
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
