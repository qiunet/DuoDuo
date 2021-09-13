package org.qiunet.logger.sender;

import org.qiunet.logger.enums.ProtoType;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class TcpMessage implements IMessage {
	private Charset charset = Charset.forName("UTF-8");
	private Logger logger = LoggerType.DUODUO.getLogger();
	//	private static ByteBuffer buffer = ByteBuffer.allocate(1024);
	private InetSocketAddress address;
	private SocketChannel channel;
	private ByteBuffer buffer;
	//	private byte[] message;
	private short gameId;
	private String secret;
	private List<String> msgList;

	/*TcpMessage(InetSocketAddress address, short gameId, String secret, byte[] message) {
		this.address = address;
		this.message = message;
		this.secret = secret;
		this.gameId = gameId;
	}*/

	TcpMessage(InetSocketAddress address, short gameId, String secret, List<String> msg) {
		this.address = address;
		this.msgList = msg;
		this.secret = secret;
		this.gameId = gameId;
	}

	/*@Override
	public void send() {
		try {
//			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.clear();
//			MsgHeader.completeMessageHeader(buffer, gameId, secret, (short) message.length);
			buffer.put(message);
			buffer.flip();
			channel.write(buffer);
			*//*try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*//*
//			System.out.println("\t msg:" + this.getMsg());
		} catch (IOException e) {
			e.printStackTrace();
			try {
				channel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}*/

	@Override
	public void send() {
		try {
			for (String msg : msgList) {
				buffer.clear();
//			MsgHeader.completeMessageHeader(buffer, gameId, secret, (short) message.length);
				buffer.put(msg.getBytes(charset));
				buffer.flip();
//				channel.write(buffer);
				while (channel.write(buffer) <= 0) {
					//由于网络层发送队列满，所以这个时候需要等网络层发送队列把已有的数据发送的，那么在这个等待的时候可以做一些其他工作
					//在循环体内写一些在等待时可以做的其他工作
				}
//				System.out.print("\t msg:" + msg);
			}

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
//		return new String(message);
		return String.valueOf(msgList.size());
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
