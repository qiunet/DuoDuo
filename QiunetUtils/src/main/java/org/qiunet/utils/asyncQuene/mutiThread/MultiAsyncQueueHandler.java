package org.qiunet.utils.asyncQuene.mutiThread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;

/**
 * 多线程异步执行队列
 * @author qiunet
 *
 * @param
 */
public class MultiAsyncQueueHandler extends ThreadPoolExecutor{
	/**
	 * 建一个指定 corePoolSize (至少保留数) 等参数的多线程执行池. 一般使用静态变量持有即可
	 * @param threadName (线程名称)
	 * @param corePoolSize  最少保留线程数
	 * @param maximumPoolSize 最大线程数
	 * @param keepAliveTime 保持活跃时间
	 * @param unit 单位
	 */
	public MultiAsyncQueueHandler(
			String threadName,
			int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit){
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>(2048),new DefaultThreadFactory(threadName+"Pool"), new DefaultExecutorRejectHandler(threadName+"Pool"));
	}
	/**
	 * 建立一个默认 最小100 最大 1024 活跃时间60秒的线程池.
	 * @param threadName
	 */
	public MultiAsyncQueueHandler(String threadName){
		this(threadName, 10, 1024, 60, TimeUnit.SECONDS);
	}

	public void addElement(Runnable element){
		this.submit(element);
	}

	public int size(){
		return this.getQueue().size();
	}
}
