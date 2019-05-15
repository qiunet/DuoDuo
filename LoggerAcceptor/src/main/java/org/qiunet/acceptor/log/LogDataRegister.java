package org.qiunet.acceptor.log;


import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/***
 * 日志数据临时寄存器
 */
public class LogDataRegister {
	private AtomicLong totalLogCount = new AtomicLong();
	private final ThreadPoolExecutor executorService;

	private volatile static LogDataRegister instance;

	private LogDataRegister() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		executorService = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new DefaultThreadFactory("LogDataRegister"));
		instance = this;
	}

	public static LogDataRegister getInstance() {
		if (instance == null) {
			synchronized (LogDataRegister.class) {
				if (instance == null)
				{
					new LogDataRegister();
				}
			}
		}
		return instance;
	}

	/***
	 * 添加一个日志数据到
	 * @param node
	 */
	public void addLogNode(LogData node) {
		totalLogCount.incrementAndGet();
		this.executorService.submit(node);
	}

	/**
	 * 得到处理的所有日志数量
	 * @return
	 */
	public long getTotalCount(){
		return totalLogCount.get();
	}

	public int size(){
		return executorService.getQueue().size();
	}
}
