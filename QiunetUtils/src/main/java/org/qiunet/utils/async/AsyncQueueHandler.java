package org.qiunet.utils.async;

import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步队列处理
 * @author qiunet
 *
 */
public class AsyncQueueHandler<T extends IQueueElement> {
	protected Logger logger = LoggerType.DUODUO.getLogger();

	private ThreadPoolExecutor executorService;

	private AsyncQueueHandler(String threadName){
		Executors.newSingleThreadExecutor();
		executorService = new ThreadPoolExecutor(
			1,
			1,
			0L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>(),
			new DefaultThreadFactory(threadName),
			new ThreadPoolExecutor.CallerRunsPolicy()
		);

		ShutdownHookThread.getInstance().addShutdownHook(this::shutdownNow);
	}


	public static <T extends IQueueElement> AsyncQueueHandler<T> create(String  threadName) {
		return new AsyncQueueHandler(threadName);
	}

	public static <T extends IQueueElement> AsyncQueueHandler<T> create() {
		return new AsyncQueueHandler("AsyncQueueHandler");
	}

	/***
	 * 停止 但是等执行完队列所有任务
	 */
	public void shutdown() {
		this.executorService.shutdown();
		try {
			if (!this.executorService.awaitTermination(30, TimeUnit.SECONDS)){
				this.shutdownNow();
			}
		} catch (InterruptedException e) {
			logger.error("shutdown timeout.", e);
			this.shutdownNow();
		}
	}
	/***
	 * 立即停止 返回没有执行完成的任务
	 * @return
	 */
	public List<Runnable> shutdownNow(){
		return this.executorService.shutdownNow();
	}
	/***
	 * 添加element   会自动调用element.handler()
	 * @param element
	 */
	public void addElement(T element){
		if (element == null) {
			throw new NullPointerException("element can not be null!");
		}
		executorService.submit(() -> element.handler());
	}
	/**
	 * 获得当前的队列size
	 * @return
	 */
	public int size(){
		return executorService.getQueue().size();
	}
}
