package org.qiunet.utils.asyncQuene;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步队列处理
 * @author qiunet
 *
 */
public class AsyncQueueHandler<T extends QueueElement> {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	// 线程计数
	private static final AtomicInteger threadNum = new AtomicInteger();

	private final Thread msgThread;

	private boolean RUNNING = true;
	// 需要完成才能停止
	private boolean needComplete;
	/*队列*/
	private final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

	private AsyncQueueHandler(String threadName){
		this.msgThread = new Thread(new HandlerTHread(), threadName);
		this.msgThread.setDaemon(true);
		this.msgThread.start();
	}


	public static <T extends QueueElement> AsyncQueueHandler<T> create(String  threadName) {
		return new AsyncQueueHandler(threadName);
	}

	public static <T extends QueueElement> AsyncQueueHandler<T> create() {
		return new AsyncQueueHandler("AsyncQueueHandler-"+threadNum.incrementAndGet());
	}

	public void shutdown() {
		this.RUNNING = false;
	}

	private Thread currThread;
	/***
	 * 处理完毕shutdown
	 */
	public void completeAndShutdown() {
		this.needComplete = true;
		currThread = Thread.currentThread();
		LockSupport.park();
	}

	/**
	 * 循环的条件
	 * @return
	 */
	protected boolean running(){
		boolean ret = RUNNING;
		if (needComplete) {
			ret = !queue.isEmpty();
			logger.info("==================size["+queue.size()+"]=========="+ret);
		}
		return ret;
	}
	/***
	 * 添加element   会自动调用element.handler()
	 * @param element
	 */
	public void addElement(T element){
		if (element == null) {
			throw new NullPointerException("element can not be null!");
		}
		queue.add(element);
	}
	/**
	 * 获得当前的队列size
	 * @return
	 */
	public int size(){
		return queue.size();
	}

	private class HandlerTHread implements Runnable{
		@Override
		public void run() {
			while(running()){
				boolean success = false;
				T element = null;
				try {
					element = queue.take();
					success = element.handler();
				}catch (Exception e){
					logger.error("[AsyncQueueHandler]出现异常"+e.getMessage());
				}finally{
					if(!success) {
						logger.error(element.toStr());
					}
				}
			}
			if (currThread != null) {
				logger.info("Thread ["+currThread.getName()+"] needComplete["+needComplete+"] queueSize["+queue.size()+"] " );
				LockSupport.unpark(currThread);
			}
		}
	}
}
