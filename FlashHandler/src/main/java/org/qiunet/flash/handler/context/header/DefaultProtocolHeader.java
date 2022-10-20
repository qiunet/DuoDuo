package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.pool.ObjectPool;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @author qiunet
 * 2022/10/20 08:55
 */
public enum DefaultProtocolHeader implements IProtocolHeader {
	instance;
	public static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 请求时候. 本端计数的key
	 */
	private static final AttributeKey<AtomicInteger> SEQUENCE_COUNTER_KEY = AttributeKey.newInstance("sequence_counter_key");
	/**
	 * 接收到的最大值
	 */
	private static final AttributeKey<Integer> REQ_SEQUENCE_KEY = AttributeKey.newInstance("req_sequence_key");

	private static final ObjectPool<ServerReqHeader> SERVER_REQ_RECYCLER = new ObjectPool<>() {
		@Override
		public ServerReqHeader newObject(Handle<ServerReqHeader> handler) {
			return new ServerReqHeader(handler);
		}
	};

	private static final ObjectPool<ServerRspHeader> SERVER_RSP_RECYCLER = new ObjectPool<>() {
		@Override
		public ServerRspHeader newObject(Handle<ServerRspHeader> handler) {
			return new ServerRspHeader(handler);
		}
	};

	@Override
	public int getServerInHeadLength() {
		return ServerReqHeader.HEADER_LENGTH;
	}

	@Override
	public int getClientInHeadLength() {
		return ServerRspHeader.HEADER_LENGTH;
	}

	@Override
	public int getConnectInHeadLength() {
		return ServerConnectHeader.HEADER_LENGTH;
	}

	@Override
	public byte[] getConnectInMagic() {
		return ServerConnectHeader.MAGIC;
	}

	@Override
	public IConnectInHeader serverConnectIn(ByteBuf in, Channel channel) {
		return ServerConnectHeader.valueOf(in, channel);
	}

	@Override
	public IConnectOutHeader clientConnectOut(IChannelMessage<?> message, Channel channel) {
		return ServerConnectHeader.valueOf(message, channel);
	}

	@Override
	public IServerInHeader serverNormalIn(ByteBuf in, Channel channel) {
		return ServerReqHeader.valueOf(in, channel);
	}

	@Override
	public IClientInHeader clientNormalIn(ByteBuf in, Channel channel) {
		return ServerRspHeader.valueOf(in, channel);
	}

	@Override
	public IClientOutHeader clientNormalOut(IChannelMessage<?> message, Channel channel) {
		return ServerReqHeader.valueOf(message, channel);
	}

	@Override
	public IServerOutHeader serverNormalOut(IChannelMessage<?> message, Channel channel) {
		return ServerRspHeader.valueOf(message, channel);
	}

	private static class ServerConnectHeader implements IConnectInHeader, IConnectOutHeader {
		private static final byte [] MAGIC = {'F', 'l', 'a', 's', 'h', 'M', 'a', 'n'};
		private final byte [] magic = new byte[MAGIC.length];

		private static final int HEADER_LENGTH = MAGIC.length + 4 + 4 + 2;
		private int protocolId;
		private int length;

		private int sequence;


		public static ServerConnectHeader valueOf(ByteBuf in, Channel channel){
			ServerConnectHeader data = new ServerConnectHeader();
			in.readBytes(data.magic);
			data.protocolId = in.readInt();
			data.length = in.readUnsignedShort();
			data.sequence = in.readInt();

			if (channel != null && ! channel.attr(REQ_SEQUENCE_KEY).compareAndSet(null, data.sequence)) {
				// 表示无效
				data.sequence = -1;
			}
			return data;
		}


		public static ServerConnectHeader valueOf(IChannelMessage<?> message, Channel channel) {
			ServerConnectHeader data = new ServerConnectHeader();

			if (channel != null) {
				AtomicInteger atomicInteger = new AtomicInteger(MathUtil.random(1000000));
				channel.attr(SEQUENCE_COUNTER_KEY).set(atomicInteger);
				data.sequence = atomicInteger.incrementAndGet();
			}

			System.arraycopy(MAGIC, 0, data.magic, 0, MAGIC.length);
			data.length = message.byteBuffer().limit();
			data.protocolId = message.getProtocolID();
			return data;
		}

		@Override
		public void recycle() {}

		@Override
		public int getProtocolId() {
			return protocolId;
		}

		@Override
		public int getLength() {
			return length;
		}

		@Override
		public ByteBuf headerByteBuf() {
			ByteBuf out = PooledByteBufAllocator.DEFAULT.buffer(HEADER_LENGTH);
			out.writeBytes(magic);
			out.writeInt(protocolId);
			out.writeShort(length);
			out.writeInt(sequence);
			return out;
		}

		@Override
		public boolean validEncryption(ByteBuffer buffer) {
			return true;
		}

		@Override
		public boolean isValidMessage() {
			// connectIn 的包的sequence 必须不为负数
			return this.sequence >= 0 && Arrays.equals(MAGIC, this.magic);
		}
	}

	private static class ServerRspHeader implements IServerOutHeader, IClientInHeader {
		private final ObjectPool.Handle<ServerRspHeader> recyclerHandle;

		private static final int HEADER_LENGTH = 6;
		private int protocolId;

		private int length;
		private ServerRspHeader(ObjectPool.Handle<ServerRspHeader> recyclerHandle) {
			this.recyclerHandle = recyclerHandle;
		}

		public static ServerRspHeader valueOf(ByteBuf in, Channel channel) {
			ServerRspHeader header = SERVER_RSP_RECYCLER.get();
			header.protocolId = in.readInt();
			header.length = in.readUnsignedShort();
			return header;
		}

		public static ServerRspHeader valueOf(IChannelMessage<?> message, Channel channel) {
			ServerRspHeader header = SERVER_RSP_RECYCLER.get();
			header.length = message.byteBuffer().limit();
			header.protocolId = message.getProtocolID();
			return header;
		}

		@Override
		public int getProtocolId() {
			return protocolId;
		}

		@Override
		public int getLength() {
			return length;
		}

		@Override
		public ByteBuf headerByteBuf() {
			ByteBuf out = PooledByteBufAllocator.DEFAULT.buffer(HEADER_LENGTH);
			out.writeInt(protocolId);
			out.writeShort(length);
			return out;
		}

		@Override
		public boolean validEncryption(ByteBuffer buffer) {
			return true;
		}

		@Override
		public boolean isValidMessage() {
			return true;
		}

		@Override
		public void recycle() {
			this.protocolId = 0;
			this.length = 0;
			this.recyclerHandle.recycle();
		}
	}

	private static class ServerReqHeader implements IServerInHeader, IClientOutHeader {
		private final ObjectPool.Handle<ServerReqHeader> recyclerHandle;

		private static final int HEADER_LENGTH = 10;
		private transient Channel channel;
		private int protocolId;

		private int length;

		private int sequence;

		private ServerReqHeader(ObjectPool.Handle<ServerReqHeader> recyclerHandle) {
			this.recyclerHandle = recyclerHandle;
		}

		public static ServerReqHeader valueOf(ByteBuf in, Channel channel) {
			ServerReqHeader header = SERVER_REQ_RECYCLER.get();
			header.protocolId = in.readInt();
			header.length = in.readUnsignedShort();
			header.sequence = in.readInt();
			header.channel = channel;
			return header;
		}

		public static ServerReqHeader valueOf(IChannelMessage<?> message, Channel channel) {
			ServerReqHeader header = SERVER_REQ_RECYCLER.get();
			if (channel != null) {
				AtomicInteger counter = channel.attr(SEQUENCE_COUNTER_KEY).get();
				header.sequence = counter.incrementAndGet();
			}
			header.length = message.byteBuffer().limit();
			header.protocolId = message.getProtocolID();
			return header;
		}

		@Override
		public int getProtocolId() {
			return protocolId;
		}

		@Override
		public int getLength() {
			return length;
		}

		@Override
		public ByteBuf headerByteBuf() {
			ByteBuf out = PooledByteBufAllocator.DEFAULT.buffer(HEADER_LENGTH);
			out.writeInt(protocolId);
			out.writeShort(length);
			out.writeInt(sequence);
			return out;
		}

		@Override
		public boolean validEncryption(ByteBuffer buffer) {
			return true;
		}

		@Override
		public boolean isValidMessage() {
			if (this.channel != null) {
				Integer sequence = channel.attr(REQ_SEQUENCE_KEY).get();
				if (this.sequence != sequence + 1) {
					logger.error("Invalid sequence! this.sequence {} , cache {}", this.sequence, sequence);
					return false;
				}
				channel.attr(REQ_SEQUENCE_KEY).set(this.sequence);
			}
			return true;
		}

		@Override
		public void recycle() {
			this.protocolId = 0;
			this.channel = null;
			this.sequence = 0;
			this.length = 0;
			this.recyclerHandle.recycle();
		}
	}
}
