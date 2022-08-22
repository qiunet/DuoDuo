package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.pool.ObjectPool;
import org.qiunet.utils.secret.CrcUtil;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 服务请求的固定头
 * 请求16字节. 响应8字节
 *
 * Created by qiunet.
 * 17/7/19
 */
public class ServerProtocolHeader implements IProtocolHeader {
	public static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final ObjectPool<ServerProtocolHeader> RECYCLER = new ObjectPool<ServerProtocolHeader>() {
		@Override
		public ServerProtocolHeader newObject(Handle<ServerProtocolHeader> handler) {
			return new ServerProtocolHeader(handler);
		}
	};

	private final ObjectPool.Handle<ServerProtocolHeader> recyclerHandle;
	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 16;
	/**响应头固定长度*/
	public static final int RESPONSE_HEADER_LENGTH = 8;

	/**辨别 请求使用*/
	private final byte [] magic = new byte[MAGIC_CONTENTS.length];
	// 长度
	private int length;
	// 请求的 响应的协议 id
	private int protocolId;
	// encryption code
	private int crc;

	public ServerProtocolHeader(ObjectPool.Handle<ServerProtocolHeader> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	/***
	 * 构造函数
	 * 不使用datainputstream了.  不确定外面使用的是什么.
	 * 由外面读取后 调构造函数传入
	 * @param message 后面byte数组
	 */
	public static ServerProtocolHeader valueOf(int protocolId, IChannelMessage<?> message) {
		ServerProtocolHeader header = RECYCLER.get();
		header.length = message.byteBuffer().limit();
		header.protocolId = protocolId;
		return header;
	}

	public static ServerProtocolHeader valueOf(ByteBuf in, Channel channel) {
		ServerProtocolHeader header = RECYCLER.get();
		in.readBytes(header.magic);
		header.length = in.readInt();
		header.protocolId = in.readInt();
		header.crc = in.readInt();
		return header;
	}


	@Override
	public void recycle() {
		this.protocolId = 0;
		this.length = 0;
		this.crc = 0;
		recyclerHandle.recycle();
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
	public ByteBuf headerByteBuf() {
		ByteBuf out = PooledByteBufAllocator.DEFAULT.buffer(RESPONSE_HEADER_LENGTH);
		out.writeInt(length);
		out.writeInt(protocolId);
		return out;
	}

	@Override
	public String toString() {
		return "ServerProtocolHeader{" +
				"magic=" + Arrays.toString(magic) +
				", length=" + length +
				", protocolId=" + protocolId +
				", encryption=" + crc +
				'}';
	}
}
