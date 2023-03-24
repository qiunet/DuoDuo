package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import org.qiunet.cross.actor.message.Cross2PlayerMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.pool.ObjectPool;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/***
 * 跨服的 协议处理头
 *
 * @author qiunet
 * 2022/10/20 08:55
 */
public enum CrossProtocolHeader implements IProtocolHeader {
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
		return ServerReqHeader.HEADER_LENGTH;
	}

	@Override
	public byte[] getConnectInMagic() {
		return ServerReqHeader.MAGIC;
	}

	@Override
	public IConnectInHeader serverConnectIn(ByteBuf in, Channel channel) {
		return ServerReqHeader.valueOf(in, channel);
	}

	@Override
	public IConnectOutHeader clientConnectOut(IChannelMessage<?> message, Channel channel) {
		return ServerReqHeader.valueOf(message, channel);
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

	private static class ServerReqHeader implements IServerInHeader, IClientOutHeader, IConnectInHeader, IConnectOutHeader {
		private final ObjectPool.Handle<ServerReqHeader> recyclerHandle;

			private static final byte [] MAGIC = {'F', 'a', 's', 't'};
		private static final int HEADER_LENGTH = MAGIC.length + 6;
		private final byte [] magic = new byte[MAGIC.length];

		private int protocolId;
		private int length;


		private ServerReqHeader(ObjectPool.Handle<ServerReqHeader> recyclerHandle) {
			this.recyclerHandle = recyclerHandle;
		}

		public static ServerReqHeader valueOf(ByteBuf in, Channel channel) {
			ServerReqHeader header = SERVER_REQ_RECYCLER.get();
			in.readBytes(header.magic);
			header.protocolId = in.readInt();
			header.length = in.readShort();
			return header;
		}

		public static ServerReqHeader valueOf(IChannelMessage<?> message, Channel channel) {
			ServerReqHeader header = SERVER_REQ_RECYCLER.get();
			header.length = (short) message.byteBuffer().limit();
			header.protocolId = message.getProtocolID();
			return header;
		}

		@Override
		public void recycle() {
			Arrays.fill(this.magic, (byte)0);
			this.protocolId = 0;
			this.length = 0;
			this.recyclerHandle.recycle();
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
			out.writeBytes(MAGIC);
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
			return Arrays.equals(MAGIC, this.magic);
		}
	}

	private static class ServerRspHeader implements IServerOutHeader, IClientInHeader, IPlayerCrossRspHeader {
		private final ObjectPool.Handle<ServerRspHeader> recyclerHandle;

		private static final int HEADER_LENGTH = 8;
		private int protocolId;

		private short length;

		private boolean flush;

		private boolean kcp;
		private ServerRspHeader(ObjectPool.Handle<ServerRspHeader> recyclerHandle) {
			this.recyclerHandle = recyclerHandle;
		}

		public static ServerRspHeader valueOf(ByteBuf in, Channel channel) {
			ServerRspHeader header = SERVER_RSP_RECYCLER.get();
			header.protocolId = in.readInt();
			header.length = in.readShort();
			header.flush = in.readBoolean();
			header.kcp = in.readBoolean();
			return header;
		}

		public static ServerRspHeader valueOf(IChannelMessage<?> message, Channel channel) {

			ServerRspHeader header = SERVER_RSP_RECYCLER.get();
			header.kcp = message instanceof Cross2PlayerMessage && ((Cross2PlayerMessage) message).isKcpChannel();
			header.flush = message instanceof Cross2PlayerMessage && ((Cross2PlayerMessage) message).isFlush();
			header.length = (short) message.byteBuffer().limit();
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
			out.writeBoolean(flush);
			out.writeBoolean(kcp);
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
			this.flush = false;
			this.kcp = false;
			this.length = 0;
			this.recyclerHandle.recycle();
		}

		@Override
		public boolean isFlush() {
			return flush;
		}

		@Override
		public boolean isKcp() {
			return kcp;
		}
	}
}
