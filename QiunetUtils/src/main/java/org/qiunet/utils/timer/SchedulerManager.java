package org.qiunet.utils.timer;

import org.qiunet.utils.listener.hook.ShutdownHookUtil;
import org.qiunet.utils.timer.executor.DCustomSchedule;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 调度管理
 * <p>
 * instance: 墙钟感知调度 (DateUtil / 系统时间跳变后会重算已有延迟任务)<br>
 * executor: 基于 Reactor 单调时钟的调度
 */
public enum SchedulerManager implements IScheduler {
	instance(new DCustomSchedule()),
	executor(new DNativeScheduler());

	private final IScheduler scheduler;

	SchedulerManager(IScheduler scheduler) {
		this.scheduler = scheduler;
		ShutdownHookUtil.getInstance().addShutdownHook(this.scheduler::close);
	}

	@Override
	public void close() {
		scheduler.close();
	}

	@Override
	public <T> Mono<T> createMonoTask(Callable<T> task, long delay, TimeUnit unit) {
		return scheduler.createMonoTask(task, delay, unit);
	}

	@Override
	public Disposable submitTask(Runnable task, long initDelay, long period, TimeUnit unit) {
		return scheduler.submitTask(task, initDelay, period, unit);
	}
}
