package org.qiunet.function.utils;

import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.utils.timer.TimerManager;

import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * MessageHandler 的对象心跳
 */
public class ObjectTick<Owner extends IMessageHandler<Owner>> {
	/**
	 * 该心跳暂停
	 */
	private final AtomicBoolean paused = new AtomicBoolean(true);
	/**
	 * 心跳执行的逻辑
	 */
	private final IMessage<Owner> runnable;
	/**
	 * 心跳周期
	 */
	private final int period;
	/**
	 * 心跳初始延迟
	 */
	private final int initDelay;
	/**
	 * 心跳周期单位
	 */
	private final TimeUnit unit;
	/**
	 * 心跳所有者
	 */
	private final WeakReference<Owner> owner;
	/**
	 * 调度 future
	 */
	private ScheduledFuture<?> future;

	public ObjectTick(Owner owner, IMessage<Owner> runnable, int period, int initDelay, TimeUnit unit) {
		this.owner = new WeakReference<>(owner);
		this.runnable = runnable;
		this.period = period;
		this.initDelay = initDelay;
		this.unit = unit;
	}
	/**
	 * 尝试调度
	 */
	public void trySchedule() {
		if (! paused.compareAndSet(true, false)) {
			return;
		}

		if (owner.get() == null) {
			throw new RuntimeException("Owner is recycle!");
		}

		if (this.future != null && ! this.future.isCancelled()) {
			return;
		}

		this.future = TimerManager.executor.scheduleAtFixedRate(() -> {
			if (paused.get()) {
				return;
			}
			Owner owner = this.owner.get();
			if (owner == null) {
				this.tryPaused();
				return;
			}

			owner.addMessage(runnable);
		}, initDelay, period, unit);
	}

	/**
	 * 尝试
	 */
	public void tryPaused() {
		if (! paused.compareAndSet(false, true)) {
			return;
		}

		this.future.cancel(false);
	}

	/**
	 * 是否暂停
	 * @return
	 */
	public boolean isPaused(){
		return this.paused.get();
	}
}
