package org.qiunet.utils.timer.executor;

import org.qiunet.utils.date.DateUtil;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时任务 (按 DateUtil 绝对时间戳触发)
 */
class DScheduleTask<R> implements IScheduleTask {

	transient DScheduleContainer container;

	private final CompletableFuture<R> future = new CompletableFuture<>();
	private final Mono<R> mono = Mono.fromFuture(future);
	private final AtomicBoolean cancelHookAttached = new AtomicBoolean();

	private final Callable<R> callable;
	private final long timestamp;
	private final long delay;

	DScheduleTask(Callable<R> callable, long delay) {
		this.timestamp = DateUtil.currentTimeMillis() + delay;
		this.callable = callable;
		this.delay = delay;
	}

	R run() throws Exception {
		return callable.call();
	}

	void setSuccess(R result) {
		future.complete(result);
	}

	void setFailure(Throwable throwable) {
		future.completeExceptionally(throwable);
	}

	Mono<R> getMono() {
		return mono;
	}

	/**
	 * 订阅取消时从队列移除 (只挂一次)
	 */
	void attachCancelHook(Runnable onCancel) {
		if (!cancelHookAttached.compareAndSet(false, true)) {
			return;
		}
		future.whenComplete((r, ex) -> {
			if (future.isCancelled()) {
				onCancel.run();
			}
		});
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public long getDelay() {
		return delay;
	}

	void cancel() {
		this.future.cancel(true);
	}

	@Override
	public String toString() {
		return "DScheduleTask{timestamp=" + timestamp + ", delay=" + delay + '}';
	}
}
