
package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.pool.ObjectPool;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/***
 *
 * @author qiunet
 * 2022/10/20 08:55
 */
public enum SequenceIdProtocolHeader implements IProtocolHeader {
	instance;
	public static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

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

	private static class ServerConnectHeader implements IConnectInHeader, IConnectOutHeader, ISequenceProtocolHeader {
		private static final byte [] MAGIC = {'F', 'l', 'a', 's', 'h', 'M', 'a', 'n'};
		private final byte [] magic = new byte[MAGIC.length];

		private static final int HEADER_LENGTH = MAGIC.length + 4 + 2 + 2;
		private int sequence;
		private int protocolId;
		private int length;


		public static ServerConnectHeader valueOf(ByteBuf in, Channel channel){
			ServerConnectHeader data = new ServerConnectHeader();
			in.readBytes(data.magic);
			data.sequence = in.readUnsignedShort();
			data.protocolId = in.readInt();
			data.length = in.readUnsignedShort();
			return data;
		}


		public static ServerConnectHeader valueOf(IChannelMessage<?> message, Channel channel) {
			ServerConnectHeader data = new ServerConnectHeader();

			System.arraycopy(MAGIC, 0, data.magic, 0, MAGIC.length);
			if (message.containKey(ISequenceProtocolHeader.MESSAGE_KEY)) {
				data.sequence = (int) message.get(ISequenceProtocolHeader.MESSAGE_KEY);
			}
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
			out.writeShort(sequence);
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
			// connectIn 的包的sequence 必须不为负数
			return Arrays.equals(MAGIC, this.magic);
		}

		@Override
		public int sequence() {
			return sequence;
		}
	}

	private static class ServerRspHeader implements IServerOutHeader, IClientInHeader, ISequenceProtocolHeader {
		private final ObjectPool.Handle<ServerRspHeader> recyclerHandle;

		private static final int HEADER_LENGTH = 8;
		private int protocolId;
		private int sequence;
		private int length;
		private ServerRspHeader(ObjectPool.Handle<ServerRspHeader> recyclerHandle) {
			this.recyclerHandle = recyclerHandle;
		}

		public static ServerRspHeader valueOf(ByteBuf in, Channel channel) {
			ServerRspHeader header = SERVER_RSP_RECYCLER.get();
			header.sequence = in.readUnsignedShort();
			header.protocolId = in.readInt();
			header.length = in.readUnsignedShort();
			return header;
		}

		public static ServerRspHeader valueOf(IChannelMessage<?> message, Channel channel) {
			ServerRspHeader header = SERVER_RSP_RECYCLER.get();
			if (message.containKey(ISequenceProtocolHeader.MESSAGE_KEY)) {
				header.sequence = (int) message.get(ISequenceProtocolHeader.MESSAGE_KEY);
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
			out.writeShort(sequence);
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
			this.sequence = 0;
			this.length = 0;
			this.recyclerHandle.recycle();
		}

		@Override
		public int sequence() {
			return sequence;
		}
	}

	private static class ServerReqHeader implements IServerInHeader, IClientOutHeader, ISequenceProtocolHeader{
		private final ObjectPool.Handle<ServerReqHeader> recyclerHandle;

		private static final int HEADER_LENGTH = 8;
		private transient Channel channel;

		private int sequence;

		private int protocolId;

		private int length;


		private ServerReqHeader(ObjectPool.Handle<ServerReqHeader> recyclerHandle) {
			this.recyclerHandle = recyclerHandle;
		}

		public static ServerReqHeader valueOf(ByteBuf in, Channel channel) {
			ServerReqHeader header = SERVER_REQ_RECYCLER.get();
			header.sequence = in.readUnsignedShort();
			header.protocolId = in.readInt();
			header.length = in.readUnsignedShort();
			header.channel = channel;
			return header;
		}

		public static ServerReqHeader valueOf(IChannelMessage<?> message, Channel channel) {
			ServerReqHeader header = SERVER_REQ_RECYCLER.get();
			if (message.containKey(ISequenceProtocolHeader.MESSAGE_KEY)) {
				header.sequence = (int) message.get(ISequenceProtocolHeader.MESSAGE_KEY);
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
			out.writeShort(sequence);
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
			this.channel = null;
			this.sequence = 0;
			this.length = 0;
			this.recyclerHandle.recycle();
		}

		@Override
		public int sequence() {
			return sequence;
		}
	}
}
