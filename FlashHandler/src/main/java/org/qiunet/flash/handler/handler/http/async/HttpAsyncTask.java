package org.qiunet.flash.handler.handler.http.async;

import com.google.common.base.Preconditions;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.timer.TimerManager;
import org.qiunet.utils.timer.timeout.TimeOutFuture;
import org.qiunet.utils.timer.timeout.Timeout;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/***
 * 异步http的处理
 *
 * @author qiunet
 * 2021/12/15 15:50
 */
public class HttpAsyncTask<R> {
	/**
	 * 超时返回 callable
	 */
	private final Callable<R> timeoutCallable;
	/**
	 * 结果 callable
	 */
	private final Callable<R> callable;
	/**
	 * 超时秒数
	 */
	private final int timeoutSeconds;

	public HttpAsyncTask(Callable<R> callable) {
		this(callable, 3);
	}

	public HttpAsyncTask(Callable<R> callable, int timeoutSeconds) {
		this(callable, null, timeoutSeconds);
	}

	public HttpAsyncTask(Callable<R> callable, Callable<R> timeoutCallable, int timeoutSeconds) {
		Preconditions.checkState(timeoutSeconds > 0 && timeoutSeconds < 100);
		Preconditions.checkNotNull(callable);

		this.timeoutCallable = timeoutCallable;
		this.timeoutSeconds = timeoutSeconds;
		this.callable = callable;
	}

	/**
	 * 回调结果获取
	 * @param consumer
	 */
	public void onComplete(BiConsumer<R, Throwable> consumer) {
		DFuture<R> rdFuture = TimerManager.executorNow(callable);
		final AtomicBoolean finished = new AtomicBoolean();

		TimeOutFuture timeOutFuture = Timeout.newTimeOut(f -> {
			if (!finished.compareAndSet(false, true)) {
				return;
			}
			rdFuture.cancel(false);
			if (timeoutCallable == null) {
				consumer.accept(null, new TimeoutException());
				return;
			}
			try {
				consumer.accept(timeoutCallable.call(), null);
			} catch (Exception e) {
				consumer.accept(null, e);
			}

		}, timeoutSeconds);

		rdFuture.whenComplete((r, ex) -> {
			if (!finished.compareAndSet(false, true)) {
				return;
			}

			timeOutFuture.cancel();
			consumer.accept(r, ex);
		});
	}
}
