package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.cross.actor.message.Cross2PlayerMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.pool.ObjectPool;
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
public class CrossProtocolHeader implements IProtocolHeader {
	public static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final ObjectPool<CrossProtocolHeader> RECYCLER = new ObjectPool<CrossProtocolHeader>() {
		@Override
		public CrossProtocolHeader newObject(Handle<CrossProtocolHeader> handler) {
			return new CrossProtocolHeader(handler);
		}
	};

	private final ObjectPool.Handle<CrossProtocolHeader> recyclerHandle;
	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 18;
	/**响应头固定长度*/
	public static final int RESPONSE_HEADER_LENGTH = 18;

	/**辨别 请求使用*/
	private final byte [] magic = new byte[MAGIC_CONTENTS.length];
	// 长度
	private int length;
	// 请求的 响应的协议 id
	private int protocolId;
	// encryption code
	private int crc;

	private boolean flush;
	private boolean kcp;

	public CrossProtocolHeader(ObjectPool.Handle<CrossProtocolHeader> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	/***
	 * 构造函数
	 * 不使用datainputstream了.  不确定外面使用的是什么.
	 * 由外面读取后 调构造函数传入
	 * @param message 后面byte数组
	 */
	public static CrossProtocolHeader valueOf(int protocolId, IChannelMessage<?> message) {
		CrossProtocolHeader header = RECYCLER.get();
		header.kcp = message instanceof Cross2PlayerMessage && ((Cross2PlayerMessage) message).isKcpChannel();
		header.flush = message instanceof Cross2PlayerMessage && ((Cross2PlayerMessage) message).isFlush();
		header.crc = (int) CrcUtil.getCrc32Value((ByteBuffer) message.byteBuffer().rewind());
		// 不需要. 直接写入MAGIC_CONTENTS
		//System.arraycopy(MAGIC_CONTENTS, 0, header.magic, 0, MAGIC_CONTENTS.length);
		header.length = message.byteBuffer().limit();
		header.protocolId = protocolId;
		return header;
	}

	public static CrossProtocolHeader valueOf(ByteBuf in, Channel channel) {
		CrossProtocolHeader header = RECYCLER.get();
		in.readBytes(header.magic);
		header.length = in.readInt();
		header.protocolId = in.readInt();
		header.crc = in.readInt();

		header.flush = in.readBoolean();
		header.kcp = in.readBoolean();
		return header;
	}

	@Override
	public void recycle() {
		this.protocolId = 0;
		this.flush = false;
		this.kcp = false;
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

	public boolean isFlush() {
		return flush;
	}

	public boolean isKcp() {
		return kcp;
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
		out.put((byte) (flush ? 1 : 0));
		out.put((byte) (kcp ? 1 : 0));
		return out;
	}

	@Override
	public String toString() {
		return "CrossProtocolHeader {" +
				"magic=" + Arrays.toString(magic) +
				", length=" + length +
				", protocolId=" + protocolId +
				", encryption=" + crc +
				'}';
	}
}
