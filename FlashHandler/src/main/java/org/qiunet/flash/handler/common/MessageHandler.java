package org.qiunet.flash.handler.common;

import com.google.common.collect.Sets;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.thread.ThreadContextData;
import org.qiunet.utils.thread.ThreadPoolManager;
import org.qiunet.utils.timer.TimerManager;
import org.qiunet.utils.timer.UseTimer;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 销毁时候, 需要一定调用 {@link MessageHandler#destroy()} !!!!!!!!!!!!!
 *
 * @author qiunet
 * 2020-02-08 20:53
 **/
public abstract class MessageHandler<H extends IMessageHandler<H>>
		implements Runnable, IMessageHandler<H>, IThreadSafe {

	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private static final ExecutorService executorService = ThreadPoolManager.MESSAGE_HANDLER;

	private final AtomicInteger size = new AtomicInteger();

	private final Queue<IMessage<H>> messages = new ConcurrentLinkedQueue<>();

	private final Set<Future<?>> scheduleFutures = Sets.newConcurrentHashSet();

	private volatile Thread currentThread;

	private final AtomicBoolean destroyed = new AtomicBoolean();

	private final UseTimer useTimer = new UseTimer(this::getIdentity, 500);

	@Override
	public void run() {
		try {
			run0();
		}finally {
			ThreadContextData.removeAll();
		}
	}

	private void run0() {
		this.currentThread = Thread.currentThread();
		long startTime = System.currentTimeMillis();
		boolean taskUseTimeToMuch = false;
		while (true) {
			IMessage<H> message = messages.poll();
			if (message == null) {
				break;
			}
			try {
				useTimer.start();
				message.execute((H) this);
				useTimer.printUseTime();
			}catch (Exception e) {
				logger.error("{}", getClass().getName(), e);
			}

			if (this.size.decrementAndGet()  <= 0) {
				break;
			}

			if (System.currentTimeMillis() - startTime > 10000) {
				taskUseTimeToMuch = true;
				break;
			}
		}
		this.currentThread = null;
		if (taskUseTimeToMuch) {
			// 重新进入排队队列， 如果线程池空闲, 会接着继续执行该Handler
			executorService.execute(this);
		}
	}

	/**
	 * 添加一条可以执行消息
	 * @param msg
	 */
	@Override
	public void addMessage(IMessage<H> msg) {
		if (this.isDestroyed()) {
			logger.error("MessageHandler [{}] 已经关闭销毁", getIdentity());
			return;
		}

		messages.add(msg);
		int size = this.size.incrementAndGet();
		if (size == 1) {
			executorService.execute(this);
		}
	}

	@Override
	public void runMessage(IMessage<H> message) {
		if (this.isDestroyed()) {
			logger.error("MessageHandler [{}] 已经关闭销毁", getIdentity());
			return;
		}

		executorService.submit(() -> message.execute((H) this));
	}


	@Override
	public boolean inSelfThread() {
		return Thread.currentThread() == currentThread;
	}

	/**
	 * 一个标识.
	 * 最好能明确的找到问题的id. 比如玩家id什么的.
	 * @return
	 */
	public String getIdentity(){
		return StringUtil.format("({0}:{1})", getClass().getSimpleName(), getId());
	}

	/**
	 * 获得id
	 * @return
	 */
	public abstract long getId();
	/**
	 * 销毁时候调用
	 */
	@Override
	public void destroy(){
		if (destroyed.compareAndSet(false, true)) {
			this.cancelAllFuture(true);
		}else {
			logger.warn("{} was destroy repeated!", getIdentity());
		}
	}

	/**
	 * 是否已经销毁
	 * @return
	 */
	public boolean isDestroyed() {
		return destroyed.get();
	}
	/**
	 * 合并另一个的所有的消息
	 * 然后销毁被合并方.
	 * @return
	 */
	public void merge(MessageHandler<H> handler){
		this.messages.addAll(handler.messages);
	}

	/**
	 * 结束所有的调度
	 */
	public void cancelAllFuture(boolean mayInterruptIfRunning) {
		scheduleFutures.forEach(scheduleFuture -> scheduleFuture.cancel(mayInterruptIfRunning));
	}
	/***
	 * 循环执行某个调度
	 * @param scheduleName 调度名称
	 * @param msg 消息
	 * @param delay 延迟
	 * @param period 间隔
	 * @return
	 */
	public Future<?> scheduleAtFixedRate(String scheduleName, IMessage<H> msg, long delay, long period, TimeUnit unit) {
		ScheduledFuture<?> future = TimerManager.instance.scheduleAtFixedRate(() -> addMessage(msg), delay, period, unit);
		return new ScheduleFuture(scheduleName, future);
	}

	/**
	 * 指定一定的延迟时间后, 执行该消息
	 * @param msg 消息
	 * @param delay 延迟数
	 * @param unit 延迟单位
	 * @return
	 */
	@Override
	public DFuture<Void> scheduleMessage(IMessage<H> msg, long delay, TimeUnit unit) {
		DFuture<Void> future = TimerManager.instance.scheduleWithDelay(() -> {
			addMessage(msg);
			return null;
		}, delay, unit);
		future.whenComplete((res, e) -> this.scheduleFutures.remove(future));
		this.scheduleFutures.add(future);
		return future;
	}

	private class ScheduleFuture implements Future<Object> {
		private final String scheduleName;
		private final Future<?> future;

		ScheduleFuture(String scheduleName, Future<?> future) {
			this.scheduleName = scheduleName;
			this.future = future;

			scheduleFutures.add(this);
		}

		@Override
		public String toString() {
			return scheduleName;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			scheduleFutures.remove(this);
			return future.cancel(mayInterruptIfRunning);
		}

		@Override
		public boolean isCancelled() {
			return  future.isCancelled();
		}

		@Override
		public boolean isDone() {
			return future.isDone();
		}

		@Override
		public Object get() throws InterruptedException, ExecutionException {
			return future.get();
		}

		@Override
		public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return future.get(timeout, unit);
		}
	}
}
