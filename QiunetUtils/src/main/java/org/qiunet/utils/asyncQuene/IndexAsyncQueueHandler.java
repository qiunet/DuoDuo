package org.qiunet.utils.asyncQuene;

import org.qiunet.utils.hook.ShutdownHookThread;

/**
 * 索引分配使用的哪个 AsyncQueueHandler
 * @author qiunet
 *         Created on 17/3/14 11:22.
 */
public class IndexAsyncQueueHandler<Element extends IndexQueueElement> {

	public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
	private static final String DEFAULT_THREAD_NAME = "IndexAsyncQueueHandler_";

	private AsyncQueueHandler[] arrays;
	/**本机的线程数量*/
	private int currThreadCount;

	public IndexAsyncQueueHandler() {
		this(DEFAULT_THREAD_NAME, THREAD_COUNT);
	}
	public IndexAsyncQueueHandler(String threadPrefixName) {
		this(threadPrefixName, THREAD_COUNT);
	}
	public IndexAsyncQueueHandler(String threadPrefixName, int threadCount) {

		if (threadCount < 1) throw new Error("Thread count can not less than 1 !");

		this.currThreadCount = threadCount;

		this.arrays = new AsyncQueueHandler[threadCount];
		for (int i = 0 ; i < threadCount ; i++) {
			this.arrays[i] = AsyncQueueHandler.create(threadPrefixName+i);
		}

		ShutdownHookThread.getInstance().addShutdownHook(() -> stop());
	}

	/**
	 * 停止
	 */
	public void stop(){
		for (AsyncQueueHandler handler : arrays) handler.shutdown();
	}

	/**
	 * 添加一个QueueElement对象
	 * @param element
	 */
	public void addElement(Element element) {
		this.addElement(element.getQueueIndex(), element);
	}
	/***
	 * 添加一个QueueElement 和指定queueIndex 到队列
	 * @param queueIndex
	 * @param element
	 */
	public void addElement(int queueIndex, IQueueElement element) {
		int realIndex = Math.abs(queueIndex % currThreadCount);
		arrays[realIndex].addElement(element);
	}
}
