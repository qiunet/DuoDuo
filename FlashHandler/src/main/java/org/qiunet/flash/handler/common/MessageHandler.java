package org.qiunet.flash.handler.common;

import com.google.common.collect.Sets;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.logger.LogUtils;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.thread.ThreadContextData;
import org.qiunet.utils.thread.ThreadPoolManager;
import org.qiunet.utils.timer.TimerManager;
import org.qiunet.utils.timer.UseTimer;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 * 销毁时候, 需要一定调用 {@link MessageHandler#destroy()} !!!!!!!!!!!!!
 *
 * @author qiunet
 * 2020-02-08 20:53
 **/
public abstract class MessageHandler<H extends IMessageHandler<H>>
		implements Runnable, IMessageHandler<H>, IThreadSafe {
	private static final MessageHandlerEventLoop executorService = new MessageHandlerEventLoop(OSUtil.availableProcessors() * 2);

	private final LazyLoader<DExecutorService> executor = new LazyLoader<>(() -> executorService.getEventLoop(this.msgExecuteIndex()));

	private final UseTimer useTimer = new UseTimer(this::getIdentity, 300);
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final Queue<IMessage<H>> messages = new ConcurrentLinkedQueue<>();
	private final Set<Future<?>> scheduleFutures = Sets.newConcurrentHashSet();

	private final AtomicBoolean destroyed = new AtomicBoolean();

	private final AtomicInteger size = new AtomicInteger();
	@Override
	public void run() {
		try {
			run0();
		}finally {
			ThreadContextData.removeAll();
		}
	}

	private void run0() {
		long start = System.nanoTime();
		while (true) {
			IMessage<H> message = messages.poll();
			if (message == null) {
				break;
			}
			try {
				useTimer.start();
				message.execute((H) this);
				useTimer.printUseTime(() -> message.getClass().getName());
			}catch (Exception e) {
				logger.error("{}", getClass().getName(), e);
			}

			if (this.size.decrementAndGet()  <= 0) {
				break;
			}
			// 超过2秒执行时间. 需要让出. 给后面的队列任务执行
			if (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) >= 2) {
				executor.get().execute(this);
				break;
			}
		}
	}
	/**
	 * 添加一条可以执行消息
	 * @param msg
	 */
	@Override
	public void addMessage(IMessage<H> msg) {
		if (this.isDestroyed()) {
			logger.error(LogUtils.dumpStack("MessageHandler ["+getIdentity()+"] 已经关闭销毁"));
			return;
		}
		messages.add(msg);
		int size = this.size.incrementAndGet();
		if (size == 1) {
			executor.get().execute(this);
		}
	}

	@Override
	public void runMessage(IMessage<H> message) {
		if (this.isDestroyed()) {
			logger.error(LogUtils.dumpStack("MessageHandler ["+getIdentity()+"] 已经关闭销毁"));
			return;
		}

		ThreadPoolManager.NORMAL.execute(() -> message.execute((H) this));
	}


	@Override
	public boolean inSelfThread() {
		return executor.get().thread == Thread.currentThread();
	}

	/**
	 * 一个标识.
	 * 最好能明确的找到问题的id. 比如玩家id什么的.
	 * @return
	 */
	public String getIdentity(){
		return StringUtil.format("({0}:{1})", getClass().getSimpleName(), this.msgExecuteIndex());
	}

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
		ScheduledFuture<?> future = TimerManager.instance.scheduleAtFixedRate(() -> this.addMessage(msg), delay, period, unit);
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
	@EventListener(EventHandlerWeightType.LESS)
	private static void shutdown(ServerShutdownEventData event) {
		for (DExecutorService service : executorService.eventLoops) {
			service.shutdown();
		}
	}

	private static class MessageHandlerEventLoop {
		private final List<DExecutorService> eventLoops;

		public MessageHandlerEventLoop(int count) {
			this.eventLoops = IntStream.range(0, count)
				.mapToObj(DExecutorService::new)
			.collect(Collectors.toList());
		}

		public DExecutorService getEventLoop(Object key) {
			int i = Math.abs(Objects.requireNonNull(key).hashCode()) % eventLoops.size();
			return eventLoops.get(i);
		}
	}

	private static class DExecutorService extends ThreadPoolExecutor {
		private final String threadName;
		private Thread thread;

		public DExecutorService(int id) {
			super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
			this.threadName = "message-handler-"+id;
			this.setThreadFactory(this::newThread);
		}

		private Thread newThread(Runnable runnable) {
			this.thread = new Thread(runnable, this.threadName);
			this.thread.setDaemon(true);
			return this.thread;
		}
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
