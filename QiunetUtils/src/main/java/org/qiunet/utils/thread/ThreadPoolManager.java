package org.qiunet.utils.thread;

import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;
import org.qiunet.utils.system.OSUtil;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/***
 * 线程池管理.
 *
 * @author qiunet
 * 2020-11-03 18:15
 */
public enum ThreadPoolManager implements ExecutorService {
	NORMAL("THREAD_POOL_MANAGER_NORMAL", OSUtil.availableProcessors() * 2, 10000);
	/**
	 * threadPool Name -> executors
	 */
	private final ExecutorService executorService;
	/**
	 * 得到或者生成新的池
	 * @param poolName
	 * @return
	 */
	ThreadPoolManager(String poolName, int threadNum, int maxCap) {
		this.executorService = new ThreadPoolExecutor(threadNum, threadNum*4,
			60, TimeUnit.SECONDS,
			new LinkedBlockingDeque<>(maxCap), new DefaultThreadFactory(poolName),
			new ThreadPoolExecutor.CallerRunsPolicy());

		ShutdownHookUtil.getInstance().addShutdownHook(this.executorService::shutdown);
	}
	/**
	 * 停止所有池资源
	 */
	public static void shutdownPool() {
		for (ThreadPoolManager manager : values()) {
			manager.shutdown();
		}
	}

	@Override
	public void shutdown() {
		this.executorService.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return this.executorService.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return this.executorService.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return this.executorService.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return this.executorService.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return this.executorService.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return this.executorService.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return this.executorService.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return this.executorService.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return this.executorService.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return this.executorService.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return this.executorService.invokeAny(tasks, timeout, unit);
	}

	@Override
	public void execute(Runnable command) {
		this.executorService.execute(command);
	}
}
