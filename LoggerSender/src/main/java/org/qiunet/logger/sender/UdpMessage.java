package org.qiunet.logger.sender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

class UdpMessage implements IMessage {
	private static ByteBuffer buffer = ByteBuffer.allocate(1024);
	private InetSocketAddress address;
	private byte[] message;
	private short gameId;

	 UdpMessage(InetSocketAddress address, short gameId,byte[] message) {
		this.address = address;
		this.message = message;
		this.gameId = gameId;
	}
	@Override
	public void send() {
		DatagramChannel channel = null;
		try {
			channel = DatagramChannel.open();
			buffer.clear();
			MsgHeader.completeMessageHeader(buffer, gameId, (short) message.length);
			buffer.put(message);
			buffer.flip();
			channel.send(buffer, address);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
