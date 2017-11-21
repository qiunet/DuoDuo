package org.qiunet.utils.nonSyncQuene;

/**
 * 索引分配使用的哪个 NonSyncQueueHandler
 * @author qiunet
 *         Created on 17/3/14 11:22.
 */
public class IndexNonSyncQueueHandler<Element extends QueueElement> {

	private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	private NonSyncQueueHandler[] arrays;
	/**本机的线程数量*/
	private int currThreadCount;

	public IndexNonSyncQueueHandler (boolean daemon ) {
		this(THREAD_COUNT, daemon);
	}
	public IndexNonSyncQueueHandler (int threadCount , boolean daemon ) {

		if (threadCount < 1) throw new Error("Thread count can not less than 1 !");

		this.currThreadCount = threadCount;

		this.arrays = new NonSyncQueueHandler[threadCount];
		for (int i = 0 ; i < threadCount ; i++) {
			this.arrays[i] = NonSyncQueueHandler.create("IndexNonSyncQueueHandler["+i+"]: ", daemon);
		}
	}

	/**
	 * 停止
	 */
	public void stop(){
		for (NonSyncQueueHandler handler : arrays) handler.shutdown();
	}

	/**
	 * 添加一个QueueElement对象
	 * @param element
	 */
	public void addElement(Element element, int index) {
		int realIndex = Math.abs(index % currThreadCount);
		arrays[realIndex].addElement(element);
	}
}
