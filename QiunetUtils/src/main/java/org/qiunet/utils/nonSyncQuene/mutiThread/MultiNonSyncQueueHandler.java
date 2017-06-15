package org.qiunet.utils.nonSyncQuene.mutiThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;

/**
 * 多线程异步执行队列
 * @author qiunet
 *
 * @param
 */
public class MultiNonSyncQueueHandler implements Runnable{
	private LinkedBlockingQueue<Runnable> queues = new LinkedBlockingQueue<Runnable>();
	private ExecutorService execute;
	private boolean running;
	private Logger logger;
	/**
	 * 建一个指定 corePoolSize (至少保留数) 等参数的多线程执行池. 一般使用静态变量持有即可
	 * @param threadName (线程名称)
	 * @param corePoolSize  最少保留线程数
	 * @param maximumPoolSize 最大线程数
	 * @param keepAliveTime 保持活跃时间
	 * @param unit 单位
	 */
	public MultiNonSyncQueueHandler(
			String threadName,
			int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit){
		execute = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(2048),new DefaultThreadFactory(threadName+"Pool"), new DefaultExecutorRejectHandler(threadName+"Pool"));
		this.logger = Logger.getLogger(threadName+"--ThreadPool");
		Thread t = new Thread(this, threadName);
		this.running = true;
		t.setDaemon(true);
		t.start();
	}
	/**
	 * 建立一个默认 最小100 最大 1024 活跃时间60秒的线程池.
	 * @param threadName
	 */
	public MultiNonSyncQueueHandler(String threadName){
		this(threadName, 10, 1024, 60, TimeUnit.SECONDS);
	}
	@Override
	public void run() {
		while(running){
			try {
				execute.execute(queues.take());
			} catch (InterruptedException e) {
				logger.error("Exception",e);
			}
		}
	}
	public void addElement(Runnable element){
		queues.add(element);
	}

	public int size(){
		return queues.size();
	}

	public void stop(){
		this.running = false;
	}
}
