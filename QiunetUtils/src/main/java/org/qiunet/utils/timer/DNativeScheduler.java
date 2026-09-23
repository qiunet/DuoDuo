package org.qiunet.utils.timer;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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
	public Disposable submitTask(Runnable task, long initDelay, long period, TimeUnit unit, AtomicLong nextFireMillis) {
		long periodMs = unit.toMillis(period);
		long start = System.currentTimeMillis() + unit.toMillis(initDelay);
		if (nextFireMillis != null) {
			nextFireMillis.set(start);
		}
		return Flux.interval(Duration.ofMillis(unit.toMillis(initDelay)), Duration.ofMillis(periodMs))
			.doOnNext(tick -> {
				if (nextFireMillis != null) {
					nextFireMillis.set(System.currentTimeMillis() + periodMs);
				}
			})
			.subscribe(ignored -> task.run());
	}
}
