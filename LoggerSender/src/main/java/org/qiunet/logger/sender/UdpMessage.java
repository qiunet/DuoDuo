package org.qiunet.logger.sender;

import org.qiunet.logger.enums.ProtoType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;

class UdpMessage implements IMessage {
	private static ByteBuffer buffer = ByteBuffer.allocate(1024);
	private InetSocketAddress address;
	private byte[] message;
	private short gameId;
	private String secret;

	UdpMessage(InetSocketAddress address, short gameId, String secret, byte[] message) {
		this.address = address;
		this.message = message;
		this.gameId = gameId;
		this.secret = secret;
	}

	@Override
	public void send() {
		DatagramChannel channel = null;
		try {
			channel = DatagramChannel.open();
			buffer.clear();
			MsgHeader.completeMessageHeader(buffer, gameId, secret, (short) message.length);
			buffer.put(message);
			buffer.flip();
			channel.send(buffer, address);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getMsg() {
		return new String(message);
	}

	@Override
	public void loadChannel(long threadId, Map<Long, SocketChannel> channelMap) {

	}

	@Override
	public ProtoType getType() {
		return ProtoType.UDP;
	}
}
