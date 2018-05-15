package org.qiunet.logger.sender;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpMessage implements IMessage {
	private static ByteBuffer buffer = ByteBuffer.allocate(1024);

	private SocketChannel channel;
	private byte [] message;
	private short gameId;
	TcpMessage(SocketChannel channel, short gameId, byte[] message)
	{
		this.channel = channel;
		this.message = message;
		this.gameId = gameId;
	}

	@Override
	public void send() {
		try {
			buffer.clear();
			MsgHeader.completeMessageHeader(buffer, gameId, (short) message.length);
			buffer.put(message);
			buffer.flip();
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
