package org.qiunet.utils.nonSyncQuene;

import java.util.concurrent.LinkedTransferQueue;

import org.apache.log4j.Logger;
import org.qiunet.utils.math.MathUtil;

/**
 * 异步队列处理
 * @author qiunet
 *
 */
public class NonSyncQueueHandler<T extends QueueElement> {
	private static final Logger logger = Logger.getLogger(NonSyncQueueHandler.class);
	private final Thread msgThread;
	private boolean RUNNING = true;
	/*队列*/
	private final LinkedTransferQueue<T> queue = new LinkedTransferQueue<>();

	private NonSyncQueueHandler(String threadName, boolean daemon){
		this.msgThread = new Thread(new HandlerTHread(), threadName);
		this.msgThread.setDaemon(daemon);
		this.msgThread.start();
	}
	
	private NonSyncQueueHandler(boolean daemon){
		this(String.valueOf("Thread-"+MathUtil.random(10000)), daemon);
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
			while(RUNNING){
				T element = queue.poll();
				boolean success = false;
				try {
					if(element != null ) success = element.handler();
				}catch (Exception e){
					// 出现异常, 不捕获. 会导致线程停止了
				}finally{
					if(!success && element != null) {
						logger.error(element.toString());
					}
				}
			}
		}
	}
}
