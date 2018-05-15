package org.qiunet.acceptor.log;

import org.qiunet.utils.asyncQuene.mutiThread.MultiAsyncQueueHandler;

import java.util.concurrent.atomic.AtomicLong;

/***
 * 日志数据临时寄存器
 */
public class LogDataRegister {
	private AtomicLong totalLogCount = new AtomicLong();
	private final MultiAsyncQueueHandler multiNonSyncQueueHandler;

	private volatile static LogDataRegister instance;

	private LogDataRegister() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		multiNonSyncQueueHandler = new MultiAsyncQueueHandler("LogDataRegister");
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
		this.multiNonSyncQueueHandler.addElement(node);
	}

	/**
	 * 得到处理的所有日志数量
	 * @return
	 */
	public long getTotalCount(){
		return totalLogCount.get();
	}
	/***
	 * 得到当前寄存器的日志数
	 * @return
	 */
	public int size(){
		return multiNonSyncQueueHandler.size();
	}
}
