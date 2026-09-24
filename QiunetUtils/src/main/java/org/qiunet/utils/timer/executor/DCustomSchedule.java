package org.qiunet.utils.timer.executor;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.timer.IScheduler;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * 逻辑时间感知调度: 延迟/周期任务按 {@link DateUtil} 绝对时间排队,
 * 逻辑时间偏移变化后由 {@link org.qiunet.utils.timer.SystemTimeWatcher} 触发重算.
 */
public class DCustomSchedule implements IScheduler {

	private final DScheduleContainer container = new DScheduleContainer(this);

	private static final BiConsumer<IScheduleTask, Throwable> DEFAULT_EXCEPTION_HANDLER = (task, throwable) ->
		LoggerType.DUODUO.error("DCustomSchedule execute task " + task + " error!", throwable);

	private final ExecutorService executorService;
	private final BiConsumer<IScheduleTask, Throwable> exceptionHandler;

	public DCustomSchedule() {
		this(OSUtil.availableProcessors() * 2);
	}

	public DCustomSchedule(int nThreads) {
		this(Executors.newFixedThreadPool(nThreads), DEFAULT_EXCEPTION_HANDLER);
	}

	public DCustomSchedule(ExecutorService executorService, BiConsumer<IScheduleTask, Throwable> exceptionHandler) {
		this.executorService = executorService;
		this.exceptionHandler = exceptionHandler;
	}

	void runTask(DScheduleTask<?> task) {
		executorService.execute(() -> runTask0(task));
	}

	private <T> void runTask0(DScheduleTask<T> task) {
		try {
			T result = task.run();
			task.setSuccess(result);
		} catch (Throwable e) {
			this.exceptionHandler.accept(task, e);
			task.setFailure(e);
		}
	}

	@Override
	public void close() {
		container.close();
		executorService.shutdown();
	}

	@Override
	public <T> Mono<T> createMonoTask(Callable<T> callable, long delay, TimeUnit unit) {
		if (delay <= 0) {
			return this.createMonoTask(callable);
		}
		DScheduleTask<T> task = new DScheduleTask<>(callable, unit.toMillis(delay));
		container.offer(task);
		return task.getMono();
	}

	/**
	 * 逻辑时间感知的周期任务: 下次触发点按 DateUtil 绝对时间推进; 偏移快进后由容器提前唤醒.
	 * @param nextFireMillis 可空; 非空时与外部共享下次触发时间, 供 ScheduledFuture#getDelay 使用
	 */
	@Override
	public Disposable submitTask(Runnable task, long initDelay, long period, TimeUnit unit, AtomicLong nextFireMillis) {
		long periodMs = unit.toMillis(period);
		long initDelayMs = unit.toMillis(initDelay);
		AtomicBoolean cancelled = new AtomicBoolean();
		AtomicReference<Disposable> current = new AtomicReference<>();
		AtomicLong nextFire = nextFireMillis != null
			? nextFireMillis
			: new AtomicLong();
		nextFire.set(DateUtil.currentTimeMillis() + initDelayMs);

		armNext(task, periodMs, cancelled, current, nextFire);
		return () -> {
			cancelled.set(true);
			Disposable d = current.getAndSet(null);
			if (d != null) {
				d.dispose();
			}
		};
	}

	/**
	 * 按 nextFire 预约下一次执行; 执行结束后再预约, 形成周期.
	 */
	private void armNext(Runnable task, long periodMs, AtomicBoolean cancelled,
						 AtomicReference<Disposable> current, AtomicLong nextFire) {
		if (cancelled.get()) {
			return;
		}
		long delayMs = Math.max(0L, nextFire.get() - DateUtil.currentTimeMillis());
		Disposable d = createMonoTask(() -> {
			if (cancelled.get()) {
				return null;
			}
			try {
				task.run();
			} finally {
				if (!cancelled.get()) {
					nextFire.addAndGet(periodMs);
					long now = DateUtil.currentTimeMillis();
					while (nextFire.get() <= now) {
						nextFire.addAndGet(periodMs);
					}
					armNext(task, periodMs, cancelled, current, nextFire);
				}
			}
			return null;
		}, delayMs, TimeUnit.MILLISECONDS).subscribe();
		current.set(d);
		if (cancelled.get()) {
			d.dispose();
		}
	}
}
