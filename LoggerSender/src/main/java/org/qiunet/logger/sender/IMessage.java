package org.qiunet.logger.sender;


import org.qiunet.logger.enums.ProtoType;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

interface IMessage {

	void send();
	String getMsg();
	ProtoType getType();
	void loadChannel(long threadId, Map<Long, SocketChannel> channelMap, Map<Long, ByteBuffer> bufferMap);
}
