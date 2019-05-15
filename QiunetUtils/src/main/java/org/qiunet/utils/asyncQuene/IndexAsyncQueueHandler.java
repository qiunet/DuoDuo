package org.qiunet.utils.asyncQuene;

import org.qiunet.utils.hook.ShutdownHookThread;

import java.util.stream.Stream;

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

		ShutdownHookThread.getInstance().addShutdownHook(() -> Stream.of(arrays).forEach(AsyncQueueHandler::shutdownNow));
	}

	/**
	 * 停止 但是会等待执行完成所有任务
	 */
	public void shutdown(){
		Stream.of(arrays).forEach(AsyncQueueHandler::shutdown);
	}
	/**
	 * 立即停止
	 */
	public void shutdownNow(){
		Stream.of(arrays).forEach(AsyncQueueHandler::shutdownNow);
	}
	/**
	 * 添加一个QueueElement对象
	 * @param element
	 */
	public void addElement(Element element) {
		this.addElement(element.getQueueIndex(), element);
	}

	/***
	 * 获得排队任务的数量
	 * @return
	 */
	public int size(){
		return Stream.of(arrays).mapToInt(AsyncQueueHandler::size).sum();
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
