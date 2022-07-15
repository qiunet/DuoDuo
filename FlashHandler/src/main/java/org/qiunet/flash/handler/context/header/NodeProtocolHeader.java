package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.secret.CrcUtil;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 服务间请求的固定头
 * 请求16字节. 响应16字节
 *
 * Created by qiunet.
 * 17/7/19
 */
public class NodeProtocolHeader implements IProtocolHeader {
	public static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 16;
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
	 * @param message 后面byte数组
	 */
	public NodeProtocolHeader(int protocolId, IChannelMessage<?> message) {
		this.crc = (int) CrcUtil.getCrc32Value((ByteBuffer) message.byteBuffer().rewind());
		this.length = message.byteBuffer().limit();
		this.protocolId = protocolId;
		this.magic = MAGIC_CONTENTS;
	}

	public NodeProtocolHeader(ByteBuf in, Channel channel) {
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
	public boolean validEncryption(ByteBuffer buffer) {
		boolean ret = (int) CrcUtil.getCrc32Value(buffer) == this.crc;
		if (! ret) {
			logger.error("Invalid message encryption! server is : "+ CrcUtil.getCrc32Value(buffer) +" client is "+ this.crc);
		}
		return ret;
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
	public  ByteBuffer dataBytes() {
		ByteBuffer out = ByteBuffer.allocateDirect(RESPONSE_HEADER_LENGTH);
		out.put(MAGIC_CONTENTS);
		out.putInt(length);
		out.putInt(protocolId);
		out.putInt(crc);
		return out;
	}

	@Override
	public String toString() {
		return "NodeProtocolHeader {" +
				"magic=" + Arrays.toString(magic) +
				", length=" + length +
				", protocolId=" + protocolId +
				", encryption=" + crc +
				'}';
	}
}