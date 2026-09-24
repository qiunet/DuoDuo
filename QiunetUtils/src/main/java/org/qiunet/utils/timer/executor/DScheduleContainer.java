package org.qiunet.utils.timer.executor;

import com.google.common.base.Preconditions;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.timer.SystemTimeWatcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 延迟任务优先队列容器. 按 DateUtil 绝对时间戳调度, 并响应逻辑时间偏移变化.
 * <p>
 * 队列变更与下次唤醒重算在同一把锁内完成, 避免 offer/cancel 与 checkAndRun
 * 交错导致更早的任务丢失唤醒.
 */
class DScheduleContainer {

	private static final ScheduledExecutorService scheduledService =
		Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "DScheduleContainer");
			t.setDaemon(true);
			return t;
		});

	private final PriorityBlockingQueue<DScheduleTask<?>> queue =
		new PriorityBlockingQueue<>(11, Comparator.comparingLong(DScheduleTask::getTimestamp));

	private final Object lock = new Object();
	private final DCustomSchedule executor;
	private final Runnable timeJumpListener = this::onTimePossiblyChanged;

	private ScheduledFuture<?> future;

	DScheduleContainer(DCustomSchedule executor) {
		this.executor = executor;
		SystemTimeWatcher.instance.addListener(timeJumpListener);
	}

	/**
	 * DateUtil 偏移变化后: 立刻弹出所有已到期任务, 并按新时间重算下次唤醒.
	 */
	void onTimePossiblyChanged() {
		checkAndRun();
	}

	private void nextScheduleLocked(long delay) {
		ScheduledFuture<?> current = this.future;
		if (current != null) {
			current.cancel(false);
		}
		long safeDelay = Math.max(0L, delay);
		this.future = scheduledService.schedule(this::checkAndRun, safeDelay, TimeUnit.MILLISECONDS);
	}

	private void checkAndRun() {
		List<DScheduleTask<?>> dueTasks = new ArrayList<>();
		synchronized (lock) {
			long now = DateUtil.currentTimeMillis();
			while (true) {
				DScheduleTask<?> peek = this.queue.peek();
				if (peek == null || peek.getTimestamp() > now) {
					break;
				}
				DScheduleTask<?> task = this.queue.poll();
				if (task != null) {
					dueTasks.add(task);
				}
			}
			this.checkNextScheduleLocked();
		}
		// 业务执行放锁外, 避免长时间占用调度锁
		for (DScheduleTask<?> task : dueTasks) {
			this.executor.runTask(task);
		}
	}

	/**
	 * 必须在持有 {@link #lock} 时调用: peek 与 nextSchedule 原子完成.
	 */
	private void checkNextScheduleLocked() {
		DScheduleTask<?> peek = this.queue.peek();
		if (peek == null) {
			ScheduledFuture<?> current = this.future;
			if (current != null) {
				current.cancel(false);
				this.future = null;
			}
			return;
		}
		this.nextScheduleLocked(peek.getTimestamp() - DateUtil.currentTimeMillis());
	}

	void offer(DScheduleTask<?> task) {
		Preconditions.checkNotNull(task);
		task.container = this;
		synchronized (lock) {
			this.queue.offer(task);
			task.attachCancelHook(() -> this.cancel(task));
			if (this.queue.peek() == task) {
				// 新任务成为队头, 按它的时间重新唤醒
				this.checkNextScheduleLocked();
			}
		}
	}

	void cancel(DScheduleTask<?> task) {
		synchronized (lock) {
			boolean removed = this.queue.remove(task);
			if (removed) {
				this.checkNextScheduleLocked();
			}
		}
	}

	void close() {
		SystemTimeWatcher.instance.removeListener(timeJumpListener);
		synchronized (lock) {
			ScheduledFuture<?> current = this.future;
			if (current != null) {
				current.cancel(false);
				this.future = null;
			}
			this.queue.clear();
		}
	}
}
