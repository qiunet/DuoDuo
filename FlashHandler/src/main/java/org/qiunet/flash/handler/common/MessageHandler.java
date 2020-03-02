package org.qiunet.flash.handler.common;

import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.timer.AsyncTimerTask;
import org.qiunet.utils.timer.TimerManager;
import org.qiunet.utils.timer.UseTimer;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @author qiunet
 * 2020-02-08 20:53
 **/
public class MessageHandler<H extends MessageHandler> implements Runnable {

	private Logger logger = LoggerType.DUODUO.getLogger();

	private static final ExecutorService executorService = Executors.newFixedThreadPool(OSUtil.availableProcessors() * 2,
			new DefaultThreadFactory("MessageHandler"));

	private AtomicInteger size = new AtomicInteger();

	private Queue<IMessage<H>> messages = new ConcurrentLinkedQueue<>();

	private Queue<ScheduleFuture> scheduleFutures = new LinkedBlockingQueue<>();

	private volatile Thread currentThread;

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
	 * 销毁时候调用
	 */
	public void destory(){
		this.cancelAllFuture();
	}

	/**
	 * 结束所有的调度
	 */
	public void cancelAllFuture() {
		scheduleFutures.forEach(scheduleFuture -> scheduleFuture.cancel(true));
	}
	/***
	 * 循环执行某个调度
	 * @param scheduleName
	 * @param msg
	 * @param delay
	 * @param period
	 * @return
	 */
	public ScheduleFuture scheduleFuture(String scheduleName, IMessage<H> msg, long delay, long period) {
		AsyncTimerTask asyncTimerTask = new AsyncTimerTask() {
			@Override
			protected void asyncRun() {
				addMessage(msg);
			}
		};
		TimerManager.getInstance().scheduleAtFixedRate(asyncTimerTask, delay, period);
		return new ScheduleFuture(scheduleName, asyncTimerTask.getFuture());
	}

	/***
	 * 在某个毫秒时间戳执行
	 * @param msg
	 * @param executeTime 执行的毫秒时间戳
	 * @return
	 */
	public ScheduleFuture scheduleMessage(String scheduleName, IMessage<H> msg, long executeTime) {
		Future<Void> future = TimerManager.getInstance().scheduleWithTimeMillis(() -> {
				addMessage(msg);
				return null;
			}, executeTime);
		return new ScheduleFuture(scheduleName, future);
	}

	/**
	 * 指定一定的延迟时间后, 执行该消息
	 * @param scheduleName
	 * @param msg
	 * @param delay
	 * @param unit
	 * @return
	 */
	public ScheduleFuture scheduleMessage(String scheduleName, IMessage<H> msg, long delay, TimeUnit unit) {
		Future<Void> future = TimerManager.getInstance().scheduleWithDeley(() -> {
			addMessage(msg);
			return null;
		}, delay, unit);

		return new ScheduleFuture(scheduleName, future);
	}


	public class ScheduleFuture implements Future<Void> {
		private String scheduleName;
		private Future<Void> future;

		public ScheduleFuture(String scheduleName, Future<Void> future) {
			this.scheduleName = scheduleName;
			this.future = future;

			scheduleFutures.add(this);
		}

		public String getScheduleName() {
			return scheduleName;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
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
		public Void get() throws InterruptedException, ExecutionException {
			return future.get();
		}

		@Override
		public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return future.get(timeout, unit);
		}
	}
}
