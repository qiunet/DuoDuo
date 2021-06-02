package org.qiunet.function.utils;

import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.utils.timer.TimerManager;

import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * MessageHandler 的对象心跳
 * @param <Owner>
 */
public class ObjectTick<Type extends Enum<Type> & ObjectTick.IObjectTickType,Owner extends IMessageHandler<Owner>> {
	/**
	 * 心跳类型枚举接口
	 */
	public interface IObjectTickType{
		String name();
	}

	/**
	 * 该心跳暂停
	 */
	private final AtomicBoolean paused = new AtomicBoolean(true);
	/**
	 * 心跳执行的逻辑
	 */
	private Runnable runnable;
	/**
	 * 心跳周期
	 */
	private int period;
	/**
	 * 心跳初始延迟
	 */
	private int initDelay;
	/**
	 * 心跳周期单位
	 */
	private TimeUnit unit;
	/**
	 * 类型
	 */
	private Type type;
	/**
	 * 心跳所有者
	 */
	private WeakReference<Owner> owner;
	/**
	 * 调度 future
	 */
	private ScheduledFuture<?> future;

	private ObjectTick(){}

	/**
	 *
	 * @param owner
	 * @param initDelay
	 * @param period
	 * @param unit
	 * @param runnable
	 * @param <Owner>
	 * @return
	 */
	public static <Type extends Enum<Type> & ObjectTick.IObjectTickType, Owner extends IMessageHandler<Owner>> ObjectTick<Type, Owner> valueOf(
			Owner owner, int initDelay, int period, TimeUnit unit, Runnable runnable
	) {
		ObjectTick<Type, Owner> tick = new ObjectTick<>();
		tick.owner = new WeakReference<>(owner);
		tick.initDelay = initDelay;
		tick.runnable = runnable;
		tick.period = period;
		tick.unit = unit;
		return tick;
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

			owner.addMessage(h -> runnable.run());
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

	public Type getType() {
		return type;
	}
}
