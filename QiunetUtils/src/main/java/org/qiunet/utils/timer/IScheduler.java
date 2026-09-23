package org.qiunet.utils.timer;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 调度任务执行
 */
public interface IScheduler {
	void close();

	/**
	 * 创建立即执行的带返回值任务
	 */
	default <T> Mono<T> createMonoTask(Callable<T> task) {
		return Mono.fromCallable(task);
	}

	/**
	 * 提交立即执行的带返回值任务
	 */
	default <T> Disposable submitTask(Callable<T> task) {
		return createMonoTask(task).subscribe();
	}

	/**
	 * 创建立即执行的无返回值任务
	 */
	default Mono<Void> createMonoTask(Runnable task) {
		return Mono.fromRunnable(task);
	}

	/**
	 * 提交立即执行的无返回值任务
	 */
	default Disposable submitTask(Runnable task) {
		return createMonoTask(task).subscribe();
	}

	/**
	 * 创建延迟执行的带返回值任务
	 */
	<T> Mono<T> createMonoTask(Callable<T> task, long delay, TimeUnit unit);

	/**
	 * 提交延迟执行的带返回值任务
	 */
	default <T> Disposable submitTask(Callable<T> task, long delay, TimeUnit unit) {
		return createMonoTask(task, delay, unit).subscribe();
	}

	/**
	 * 创建延迟执行的无返回值任务
	 */
	default Mono<Void> createMonoTask(Runnable task, long delay, TimeUnit unit) {
		return createMonoTask(() -> {
			task.run();
			return null;
		}, delay, unit).then();
	}

	/**
	 * 提交延迟执行的无返回值任务
	 */
	default Disposable submitTask(Runnable task, long delay, TimeUnit unit) {
		return createMonoTask(task, delay, unit).subscribe();
	}

	/**
	 * 提交循环任务
	 */
	Disposable submitTask(Runnable task, long initDelay, long period, TimeUnit unit);
}
