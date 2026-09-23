package org.qiunet.utils.timer;

import org.qiunet.utils.date.DateUtil;
import reactor.core.Disposable;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 将 Reactor {@link Disposable} 适配为 {@link ScheduledFuture}.
 * 支持取消, 以及基于 {@link DateUtil} 的剩余延迟查询.
 */
final class DisposableScheduledFuture<V> implements ScheduledFuture<V> {

	private final AtomicBoolean cancelled = new AtomicBoolean();
	private final AtomicReference<Disposable> disposable = new AtomicReference<>();
	/**
	 * 下次触发的逻辑时间戳 (毫秒)
	 */
	private final AtomicLong nextFireMillis;
	/**
	 * 一次性任务完成信号; 周期任务仅在 cancel 时完成.
	 */
	private final CompletableFuture<V> completion = new CompletableFuture<>();

	private DisposableScheduledFuture(AtomicLong nextFireMillis, boolean ignoredPeriodic) {
		this.nextFireMillis = nextFireMillis;
	}

	static DisposableScheduledFuture<Object> createPeriodic(AtomicLong nextFireMillis) {
		return new DisposableScheduledFuture<>(nextFireMillis, true);
	}

	/**
	 * 仅作取消句柄 (延迟任务挂到 DCompletePromise 上)
	 */
	static Future<?> asCancelHandle(Disposable disposable) {
		AtomicLong nextFire = new AtomicLong(DateUtil.currentTimeMillis());
		DisposableScheduledFuture<Object> future = new DisposableScheduledFuture<>(nextFire, false);
		future.bind(disposable);
		return future;
	}

	void bind(Disposable d) {
		disposable.set(d);
		if (cancelled.get() && d != null) {
			d.dispose();
		}
	}

	@Override
	public long getDelay(TimeUnit unit) {
		if (isDone()) {
			return 0;
		}
		long delayMs = nextFireMillis.get() - DateUtil.currentTimeMillis();
		return unit.convert(delayMs, TimeUnit.MILLISECONDS);
	}

	@Override
	public int compareTo(Delayed o) {
		return Long.compare(getDelay(TimeUnit.NANOSECONDS), o.getDelay(TimeUnit.NANOSECONDS));
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (!cancelled.compareAndSet(false, true)) {
			return false;
		}
		Disposable d = disposable.getAndSet(null);
		if (d != null) {
			d.dispose();
		}
		completion.cancel(mayInterruptIfRunning);
		return true;
	}

	@Override
	public boolean isCancelled() {
		return cancelled.get();
	}

	@Override
	public boolean isDone() {
		return cancelled.get() || completion.isDone();
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		try {
			return completion.get();
		} catch (CancellationException e) {
			throw new CancellationException("schedule cancelled");
		}
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			return completion.get(timeout, unit);
		} catch (CancellationException e) {
			throw new CancellationException("schedule cancelled");
		}
	}
}
