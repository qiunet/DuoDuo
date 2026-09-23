package org.qiunet.utils.timer.executor;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.timer.IScheduler;
import org.qiunet.utils.timer.SchedulerManager;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 墙钟感知的延迟调度: 任务按 DateUtil 绝对时间排队,
 * 系统时间 / DateUtil 跳变后由 {@link org.qiunet.utils.timer.SystemTimeWatcher} 触发重算.
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

	@Override
	public Disposable submitTask(Runnable task, long initDelay, long period, TimeUnit unit) {
		// 周期任务走单调时钟调度
		return SchedulerManager.executor.submitTask(task, initDelay, period, unit);
	}
}
