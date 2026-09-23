package org.qiunet.utils.timer;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用 Reactor 随机间隔探测墙钟时间跳变.
 * 跳变后通知已注册的调度容器, 让延迟任务按新时间立刻重算/触发.
 */
public enum SystemTimeWatcher {
	instance;

	/**
	 * 超过该漂移视为时间跳变 (毫秒)
	 */
	static final long JUMP_THRESHOLD_MS = 200L;
	/**
	 * 探测间隔下限
	 */
	private static final int MIN_INTERVAL_MS = 200;
	/**
	 * 探测间隔随机附加上限
	 */
	private static final int RANDOM_INTERVAL_MS = 600;

	private final List<Runnable> listeners = new CopyOnWriteArrayList<>();
	private final AtomicBoolean started = new AtomicBoolean();

	private volatile Disposable disposable;
	private volatile long lastWallMillis;
	private volatile long lastNano;

	SystemTimeWatcher() {
		ShutdownHookUtil.getInstance().addShutdownHook(this::stop);
	}

	/**
	 * 注册跳变回调
	 */
	public void addListener(Runnable listener) {
		listeners.add(listener);
		ensureStarted();
	}

	/**
	 * 移除跳变回调
	 */
	public void removeListener(Runnable listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			stop();
		}
	}

	/**
	 * 立刻做一次探测 (测试或主动通知时使用)
	 */
	public void kick() {
		checkDrift();
	}

	private void ensureStarted() {
		if (!started.compareAndSet(false, true)) {
			return;
		}
		lastWallMillis = DateUtil.currentTimeMillis();
		lastNano = System.nanoTime();
		disposable = Flux.defer(() -> Mono.delay(nextInterval()))
			.repeat()
			.subscribe(ignored -> checkDrift());
	}

	private Duration nextInterval() {
		int delay = MIN_INTERVAL_MS + ThreadLocalRandom.current().nextInt(RANDOM_INTERVAL_MS);
		return Duration.ofMillis(delay);
	}

	private void checkDrift() {
		long wallNow = DateUtil.currentTimeMillis();
		long nanoNow = System.nanoTime();
		long expected = lastWallMillis + (nanoNow - lastNano) / 1_000_000L;
		long drift = wallNow - expected;
		lastWallMillis = wallNow;
		lastNano = nanoNow;
		if (Math.abs(drift) < JUMP_THRESHOLD_MS) {
			return;
		}
		for (Runnable listener : listeners) {
			try {
				listener.run();
			} catch (Throwable ignored) {
				// 单个监听失败不影响其它
			}
		}
	}

	private void stop() {
		Disposable d = disposable;
		disposable = null;
		started.set(false);
		if (d != null) {
			d.dispose();
		}
	}
}
