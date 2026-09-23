package org.qiunet.utils.timer;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Mono / Flux 的调度 (单调时钟, 不受墙钟跳变影响)
 */
class DNativeScheduler implements IScheduler {

	@Override
	public void close() {
		// no-op
	}

	@Override
	public <T> Mono<T> createMonoTask(Callable<T> task, long delay, TimeUnit unit) {
		return Mono.delay(Duration.ofMillis(unit.toMillis(delay)))
			.then(Mono.fromCallable(task));
	}

	@Override
	public Disposable submitTask(Runnable task, long initDelay, long period, TimeUnit unit) {
		return Flux.interval(Duration.ofMillis(unit.toMillis(initDelay)), Duration.ofMillis(unit.toMillis(period)))
			.subscribe(ignored -> task.run());
	}
}
