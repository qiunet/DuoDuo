package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.cross.node.ServerNodeServerHandler;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.response.push.IExtraInfo;
import org.qiunet.flash.handler.netty.server.node.handler.CrossPlayerNodeServerHandler;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.pool.ObjectPool;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/***
 * 兼容以前的 协议处理头
 *
 * @author qiunet
 * 2022/10/20 08:55
 */
public enum NodeProtocolHeader implements IProtocolHeader {
	instance;
	/**
	 * id
	 */
	public static final String ID_KEY = "header_id";

	public static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private static final ObjectPool<NodeServerHeader> SERVER_NODE_RECYCLER = new ObjectPool<>() {
		@Override
		public NodeServerHeader newObject(Handle<NodeServerHeader> handler) {
			return new NodeServerHeader(handler);
		}
	};

	@Override
	public void completeServerHandler(ByteBuf byteBuf, ChannelPipeline pipeline) {
		byte extraInfo = byteBuf.getByte(getConnectInMagic().length);
		if (IExtraInfo.ExtraInfoType.SERVER_NODE_MSG.gotTruth(extraInfo)) {
			pipeline.addLast("ServerNodeServerHandler", new ServerNodeServerHandler());
		}else if (IExtraInfo.ExtraInfoType.CROSS_PLAYER_MSG.gotTruth(extraInfo)){
			pipeline.addLast("PlayerNodeServerHandler", new CrossPlayerNodeServerHandler());
		}
	}

	@Override
	public int getServerInHeadLength() {
		return NodeServerHeader.HEADER_LENGTH;
	}

	@Override
	public int getClientInHeadLength() {
		return NodeServerHeader.HEADER_LENGTH;
	}

	@Override
	public int getConnectInHeadLength() {
		return NodeServerHeader.HEADER_LENGTH;
	}
	@Override
	public byte[] getConnectInMagic() {
		return NodeServerHeader.MAGIC;
	}
	@Override
	public IConnectInHeader serverConnectIn(ByteBuf in, Channel channel) {
		return NodeServerHeader.valueOf(in, channel);
	}

	@Override
	public IConnectOutHeader clientConnectOut(IChannelMessage<?> message, Channel channel) {
		return NodeServerHeader.valueOf(message, channel);
	}

	@Override
	public IServerInHeader serverNormalIn(ByteBuf in, Channel channel) {
		return NodeServerHeader.valueOf(in, channel);
	}

	@Override
	public IClientInHeader clientNormalIn(ByteBuf in, Channel channel) {
		return NodeServerHeader.valueOf(in, channel);
	}

	@Override
	public IClientOutHeader clientNormalOut(IChannelMessage<?> message, Channel channel) {
		return NodeServerHeader.valueOf(message, channel);
	}

	@Override
	public IServerOutHeader serverNormalOut(IChannelMessage<?> message, Channel channel) {
		return NodeServerHeader.valueOf(message, channel);
	}

	private static class NodeServerHeader implements IServerInHeader, IServerOutHeader, IClientOutHeader, IClientInHeader,
			IConnectInHeader, IConnectOutHeader, INodeServerHeader {
		private final ObjectPool.Handle<NodeServerHeader> recyclerHandle;
		private static final byte [] MAGIC = {'F', 'a', 's', 't'};
		private static final int HEADER_LENGTH = MAGIC.length + 21;
		private final byte [] magic = new byte[MAGIC.length];

		private int extraInfo;
		private int protocolId;
		private int length;
		private int serverId;
		private long id;
		private NodeServerHeader(ObjectPool.Handle<NodeServerHeader> recyclerHandle) {
			this.recyclerHandle = recyclerHandle;
		}

		public static NodeServerHeader valueOf(ByteBuf in, Channel channel) {
			NodeServerHeader header = SERVER_NODE_RECYCLER.get();
			in.readBytes(header.magic);
			header.extraInfo = in.readByte();
			header.id = in.readLong();
			header.serverId = in.readInt();
			header.protocolId = in.readInt();
			header.length = in.readInt();
			return header;
		}

		public static NodeServerHeader valueOf(IChannelMessage message, Channel channel) {
			NodeServerHeader header = SERVER_NODE_RECYCLER.get();
			header.serverId = ServerNodeManager.getCurrServerId();
			header.length = message.byteBuffer().limit();
			header.protocolId = message.getProtocolID();
			header.extraInfo = message.getExtraInfo();
			Long aLong = (Long) message.get(ID_KEY);
			if (aLong != null) {
				header.id = aLong;
			}
			return header;
		}

		@Override
		public void recycle() {
			Arrays.fill(this.magic, (byte)0);
			this.protocolId = 0;
			this.extraInfo = 0;
			this.serverId = 0;
			this.length = 0;
			this.id = 0;
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
			out.writeByte(extraInfo);
			out.writeLong(id);
			out.writeInt(serverId);
			out.writeInt(protocolId);
			out.writeInt(length);
			return out;
		}

		@Override
		public int getServerId() {
			return serverId;
		}

		@Override
		public boolean validEncryption(ByteBuffer buffer) {
			return true;
		}

		@Override
		public boolean isValidMessage() {
			return Arrays.equals(MAGIC, this.magic);
		}

		@Override
		public boolean isServerNodeMsg() {
			return IExtraInfo.ExtraInfoType.SERVER_NODE_MSG.gotTruth(this.extraInfo);
		}

		@Override
		public boolean isPlayerMsg() {
			return IExtraInfo.ExtraInfoType.CROSS_PLAYER_MSG.gotTruth(this.extraInfo);
		}

		@Override
		public boolean isFlush() {
			return IExtraInfo.ExtraInfoType.FLUSH.gotTruth(this.extraInfo);
		}

		@Override
		public boolean isKcp() {
			return IExtraInfo.ExtraInfoType.KCP.gotTruth(this.extraInfo);
		}

		@Override
		public long id() {
			return id;
		}

		@Override
		public String toString() {
			return "NodeServerHeader{" +
				"extraInfo=" + extraInfo +
				", protocolId=" + protocolId +
				", length=" + length +
				", serverId=" + serverId +
				", id=" + id +
				'}';
		}
	}
}
