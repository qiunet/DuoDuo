package org.qiunet.flash.handler.common;

import com.google.common.collect.Sets;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.timer.TimerManager;
import org.qiunet.utils.timer.UseTimer;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 销毁时候, 需要一定调用 {@link MessageHandler#destory()}!!!!!!!!!!!!!
 *
 * @author qiunet
 * 2020-02-08 20:53
 **/
public abstract class MessageHandler<H extends MessageHandler> implements Runnable {

	private Logger logger = LoggerType.DUODUO.getLogger();

	private static final ExecutorService executorService = Executors.newFixedThreadPool(OSUtil.availableProcessors() * 2,
			new DefaultThreadFactory("MessageHandler"));

	private AtomicInteger size = new AtomicInteger();

	private Queue<IMessage<H>> messages = new ConcurrentLinkedQueue<>();

	private Set<Future> scheduleFutures = Sets.newConcurrentHashSet();

	private volatile Thread currentThread;

	private volatile boolean close;

	private UseTimer useTimer = new UseTimer(getClass().getName(), 500);

	@Override
	public void run() {
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

			int size = this.size.decrementAndGet();
			if (size  <= 0) {
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
	public void addMessage(IMessage<H> msg) {
		if (close) {
			logger.error("MessageHandler ["+getIdent()+"] 已经关闭销毁", new RuntimeException());
			return;
		}

		messages.add(msg);
		int size = this.size.incrementAndGet();
		if (size == 1) {
			executorService.execute(this);
		}
	}

	public boolean isInThread(){
		return Thread.currentThread() == currentThread;
	}

	/**
	 * 一个标识.
	 * 最好能明确的找到问题的id. 比如玩家id什么的.
	 * @return
	 */
	protected abstract String getIdent();
	/**
	 * 销毁时候调用
	 */
	public void destory(){
		this.cancelAllFuture(true);
		this.close = true;
	}

	/**
	 * 结束所有的调度
	 */
	public void cancelAllFuture(boolean mayInterruptIfRunning) {
		scheduleFutures.forEach(scheduleFuture -> scheduleFuture.cancel(mayInterruptIfRunning));
	}
	/***
	 * 循环执行某个调度
	 * @param scheduleName
	 * @param msg
	 * @param delay
	 * @param period
	 * @return
	 */
	public ScheduleFuture scheduleAtFixedRate(String scheduleName, IMessage<H> msg, long delay, long period, TimeUnit unit) {
		ScheduledFuture<?> future = TimerManager.getInstance().scheduleAtFixedRate(() -> addMessage(msg), delay, period, unit);
		return new ScheduleFuture(scheduleName, future);
	}

	/***
	 * 在某个毫秒时间戳执行
	 * @param msg
	 * @param executeTime 执行的毫秒时间戳
	 * @return
	 */
	public DFuture<Void> scheduleMessage(IMessage<H> msg, long executeTime) {
		long now = DateUtil.currentTimeMillis();
		if (executeTime < now) {
			throw new IllegalStateException("executeTime ["+executeTime+"] is less than current time ["+now+"]");
		}
		return this.scheduleMessage(msg, (executeTime - now), TimeUnit.MILLISECONDS);
	}

	/**
	 * 指定一定的延迟时间后, 执行该消息
	 * @param msg
	 * @param delay
	 * @param unit
	 * @return
	 */
	public DFuture<Void> scheduleMessage(IMessage<H> msg, long delay, TimeUnit unit) {
		DFuture<Void> future = TimerManager.getInstance().scheduleWithDeley(() -> {
			addMessage(msg);
			return null;
		}, delay, unit);
		future.whenComplete((res, e) -> this.scheduleFutures.remove(future));
		this.scheduleFutures.add(future);
		return future;
	}


	private class ScheduleFuture implements Future<Object> {
		private String scheduleName;
		private Future<?> future;

		public ScheduleFuture(String scheduleName, Future<?> future) {
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
