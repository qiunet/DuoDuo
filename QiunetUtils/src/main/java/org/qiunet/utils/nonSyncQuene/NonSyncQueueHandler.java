package org.qiunet.utils.nonSyncQuene;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * 异步队列处理
 * @author qiunet
 *
 */
public class NonSyncQueueHandler<T extends QueueElement> {
	private static final QLogger logger = LoggerManager.getLogger(LoggerType.QIUNET_UTILS);
	// 线程计数
	private static final AtomicInteger threadNum = new AtomicInteger();

	private final Thread msgThread;

	private boolean RUNNING = true;
	// 需要完成才能停止
	private boolean needComplete;
	/*队列*/
	private final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

	private NonSyncQueueHandler(String threadName, boolean daemon){
		this.msgThread = new Thread(new HandlerTHread(), threadName);
		/**交给外面关闭有问题, 很多人不会去关闭, 导致线程不停止*/
		if (!daemon) {
			logger.info("因为线程交给使用者关闭并非一个好决策, 所以默认使用守护模式线程. 如果需要处理完再停止, 请停止jvm前调用 completeAndShutdown() ");
		}
		this.msgThread.setDaemon(true);
		this.msgThread.start();
	}

	private NonSyncQueueHandler(boolean daemon){
		this("NonSyncQueueHandler-"+threadNum.incrementAndGet(), daemon);
	}

	public static <T extends QueueElement> NonSyncQueueHandler<T> create(String  threadName, boolean daemon) {
		return new NonSyncQueueHandler<>(threadName , daemon);
	}

	public static <T extends QueueElement> NonSyncQueueHandler<T> create(boolean daemon) {
		return new NonSyncQueueHandler<>(daemon);
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
		if (needComplete) ret = !queue.isEmpty();
		return ret;
	}
	/***
	 * 添加element   会自动调用element.handler()
	 * @param element
	 */
	public void addElement(T element){
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
					if(element != null ) success = element.handler();
				}catch (Exception e){
					// 出现异常, 不捕获. 会导致线程停止了
				}finally{
					if(!success && element != null) {
						logger.error(element.toString());
					}
				}
			}
			if (currThread != null) LockSupport.unpark(currThread);
		}
	}
}
