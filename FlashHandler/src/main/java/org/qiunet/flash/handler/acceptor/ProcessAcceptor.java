package org.qiunet.flash.handler.acceptor;

import org.qiunet.utils.asyncQuene.IndexAsyncQueueHandler;
import org.qiunet.utils.asyncQuene.IndexQueueElement;
import org.qiunet.utils.asyncQuene.QueueElement;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.system.OSUtil;

/**
 * 一个有索引的异步队列. 可以根据索引
 *
 *
 * Created by qiunet.
 * 17/7/21
 */
public class ProcessAcceptor {
	/**使用几个线程来处理*/
	private static final int THREAD_COUNT = OSUtil.availableProcessors() *2;
	/**线程的处理队列*/
	private IndexAsyncQueueHandler<IndexQueueElement> contextProcessor;

	/**
	 * 创建当前cpu核数相当的线程作为 context的处理队列
	 * @return
	 */
	private ProcessAcceptor(int threadCount) {
		if (instance != null ) {
			throw new RuntimeException("Duplicate called the ProcessAcceptor Instructor ");
		}

		if (threadCount < 1) throw new Error("ThreadCount can not less than 1 !");
		this.contextProcessor = new IndexAsyncQueueHandler("Flash_Handler_Process_Acceptor_", threadCount);

		ShutdownHookThread.getInstance().addShutdownHook(() -> this.shutdown());
		instance = this;
	}

	private volatile static ProcessAcceptor instance;
	public static ProcessAcceptor getInstance() {
		if (instance == null) {
			synchronized (ProcessAcceptor.class) {
				if (instance == null)
				{
					new ProcessAcceptor(THREAD_COUNT);
				}
			}
		}
		return instance;
	}

	/**
	 * 停止所有的线程
	 */
	public void shutdown() {
		contextProcessor.stop();
	}
	/**
	 * 请求的处理
	 * @param iContext
	 */
	public void process(IndexQueueElement iContext) {
		contextProcessor.addElement(iContext);
	}

	/***
	 * 自己指定队列索引信息的元素 处理
	 * @param queueIndex
	 * @param element
	 */
	public void process(int queueIndex, QueueElement element) {
		contextProcessor.addElement(queueIndex, element);
	}
}
