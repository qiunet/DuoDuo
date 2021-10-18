package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.secret.CrcUtil;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 测试客户端请求的固定头
 *
 * 请求8字节. 响应16字节
 *
 * Created by qiunet.
 * 17/7/19
 */
public class ClientProtocolHeader implements IProtocolHeader {
	public static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();


	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 8;
	/**响应头固定长度*/
	public static final int RESPONSE_HEADER_LENGTH = 16;

	/**辨别 请求使用*/
	private final byte [] magic;
	// 长度
	private final int length;
	// 请求的 响应的协议 id
	private final int protocolId;
	// encryption code
	private final int crc;

	/***
	 * 构造函数
	 * 不使用datainputstream了.  不确定外面使用的是什么.
	 * 由外面读取后 调构造函数传入
	 * @param content 后面byte数组
	 */
	public ClientProtocolHeader(MessageContent content) {
		this.magic = MAGIC_CONTENTS;
		this.length = content.bytes().length;
		this.protocolId = content.getProtocolId();
		this.crc = (int) CrcUtil.getCrc32Value(content.bytes());
	}

	public ClientProtocolHeader(ByteBuf in) {
		this.magic = new byte[MAGIC_CONTENTS.length];
		this.length = in.readInt();
		this.protocolId = in.readInt();
		this.crc = 0;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public boolean validEncryption(ByteBuffer buffer) {
		return true;
	}

	@Override
	public int getProtocolId() {
		return protocolId;
	}

	@Override
	public boolean isMagicValid(){
		return true;
	}

	@Override
	public  byte[] dataBytes() {
		ByteBuffer out = ByteBuffer.allocate(RESPONSE_HEADER_LENGTH);
		out.put(MAGIC_CONTENTS);
		out.putInt(length);
		out.putInt(protocolId);
		out.putInt(crc);
		return out.array();
	}

	@Override
	public String toString() {
		return "ClientProtocolHeader {" +
				"magic=" + Arrays.toString(magic) +
				", length=" + length +
				", protocolId=" + protocolId +
				", encryption=" + crc +
				'}';
	}
}