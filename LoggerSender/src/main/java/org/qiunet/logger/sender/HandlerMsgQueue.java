package org.qiunet.logger.sender;

import org.qiunet.logger.enums.ProtoType;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

class HandlerMsgQueue {
	AtomicLong atomicLong = new AtomicLong();
	private int threadNum;
	private ExecutorService executorService;
	private Map<Long, SocketChannel> channelMap;
	private Map<Long, ByteBuffer> bufferMap;

	public HandlerMsgQueue() {
	}

	public HandlerMsgQueue(int threadNum) {
		this.threadNum = threadNum;
		this.executorService = Executors.newFixedThreadPool(threadNum);
		this.channelMap = new HashMap<>(threadNum);
		this.bufferMap = new HashMap<>(threadNum);
	}

	/***
	 * 添加一个元素
	 * @param message
	 */
	void add(IMessage message) {
		this.executorService.submit(() -> {
			atomicLong.incrementAndGet();
			long threadId = Thread.currentThread().getId();
			if (message.getType() == ProtoType.TCP) {
				message.loadChannel(threadId, channelMap, bufferMap);
			}
			message.send();
//			System.out.println("threadId:" + threadId + "\t msg:" + message.getMsg());
		});
	}

	void shutdown() {
		this.executorService.shutdown();
	}
}
