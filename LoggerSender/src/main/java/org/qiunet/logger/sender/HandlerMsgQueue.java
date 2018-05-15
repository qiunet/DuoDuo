package org.qiunet.logger.sender;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

class HandlerMsgQueue implements Runnable {
	AtomicLong atomicLong = new AtomicLong();
	/*队列*/
	private final LinkedBlockingQueue<IMessage> queue = new LinkedBlockingQueue<>();

	boolean RUNNING = true;
	/***
	 * 添加一个元素
	 * @param message
	 */
	void add(IMessage message) {
		this.queue.add(message);
	}

	@Override
	public void run() {
		while(RUNNING){
			try {
				IMessage message = queue.take();
				message.send();

				Thread.sleep(1);
				atomicLong.incrementAndGet();
			}catch (Exception e){
				//出现异常, 不捕获. 会导致线程停止了
			}
		}
	}
}
