package org.qiunet.logger.sender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

class HandlerMsgQueue {
	AtomicLong atomicLong = new AtomicLong();
	private ExecutorService executorService = Executors.newFixedThreadPool(2);
	/***
	 * 添加一个元素
	 * @param message
	 */
	void add(IMessage message) {
		this.executorService.submit(() -> {
			atomicLong.incrementAndGet();
			message.send();
		});
	}

	void shutdown(){
		this.executorService.shutdown();
	}
}
