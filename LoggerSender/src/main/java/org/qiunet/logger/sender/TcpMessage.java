package org.qiunet.logger.sender;

import org.qiunet.logger.enums.ProtoType;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Map;

public class TcpMessage implements IMessage {
	private Logger logger = LoggerType.DUODUO.getLogger();
	//	private static ByteBuffer buffer = ByteBuffer.allocate(1024);
	private InetSocketAddress address;
	private SocketChannel channel;
	private ByteBuffer buffer;
	private byte[] message;
	private short gameId;
	private String secret;

	TcpMessage(InetSocketAddress address, short gameId, String secret, byte[] message) {
		this.address = address;
		this.message = message;
		this.secret = secret;
		this.gameId = gameId;
	}

	@Override
	public void send() {
		try {
			buffer.clear();
//			MsgHeader.completeMessageHeader(buffer, gameId, secret, (short) message.length);
			buffer.put(message);
			buffer.flip();
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				channel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public String getMsg() {
		return new String(message);
	}

	@Override
	public void loadChannel(long threadId, Map<Long, SocketChannel> channelMap, Map<Long, ByteBuffer> bufferMap) {
		if (channelMap.containsKey(threadId)) {
			channel = channelMap.get(threadId);
		}
		if (channel == null || !(channel.isConnected() && channel.isOpen())) {
			try {
				channel = SocketChannel.open(address);
				channel.configureBlocking(false);
				logger.info("create channel :" + threadId);
			} catch (IOException e) {
				logger.error("异常", e);
			}
			channelMap.put(threadId, channel);
		}

		if (bufferMap.containsKey(threadId)) {
			buffer = bufferMap.get(threadId);
		}
		if (buffer == null) {
			buffer = ByteBuffer.allocate(1024);
			bufferMap.put(threadId, buffer);
		}
	}

	@Override
	public ProtoType getType() {
		return ProtoType.TCP;
	}
}
