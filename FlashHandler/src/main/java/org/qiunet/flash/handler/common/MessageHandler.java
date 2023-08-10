package org.qiunet.flash.handler.common;

import com.google.common.collect.Sets;
import io.netty.util.concurrent.FastThreadLocalThread;
import org.qiunet.data.async.ISyncDbExecutor;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;
import org.qiunet.utils.logger.LogUtils;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.thread.IThreadSafe;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/***
 * 销毁时候, 需要一定调用 {@link MessageHandler#destroy()} !!!!!!!!!!!!!
 *
 * @author qiunet
 * 2020-02-08 20:53
 **/
public abstract class MessageHandler<H extends IMessageHandler<H>>
		implements IMessageHandler<H>, IThreadSafe {
	protected static final MessageHandlerEventLoop executorService = new MessageHandlerEventLoop(OSUtil.availableProcessors() * 2);

	protected final LazyLoader<DExecutorService> executor = new LazyLoader<>(() -> executorService.getEventLoop(this.msgExecuteIndex()));

	protected final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final Set<Future<?>> scheduleFutures = Sets.newConcurrentHashSet();

	private final AtomicBoolean destroyed = new AtomicBoolean();
	/**
	 * 处理异常情况
	 * @param e
	 */
	protected void exceptionHandle(Exception e) {
		logger.error("Message handler run exception:", e);
	}

	/**
	 * 得到当前MessageHandler 的 executor
	 * @return
	 */
	protected DExecutorService getExecutor() {
		return executor.get();
	}

	/**
	 * 添加一条可以执行消息
	 * @param msg
	 */
	@Override
	public boolean addMessage(IMessage<H> msg) {
		if (this.isDestroyed()) {
			logger.error(LogUtils.dumpStack("MessageHandler ["+getIdentity()+"] 已经关闭销毁"));
			return false;
		}
		this.getExecutor().execute(new Message0(this, msg));
		return true;
	}

	@Override
	public void runMessage(IMessage<H> message) {
		if (this.isDestroyed()) {
			logger.error(LogUtils.dumpStack("MessageHandler ["+getIdentity()+"] 已经关闭销毁"));
			return;
		}

		executorService.randEventLoop().execute(new Message0(this, message));
	}

	@Override
	public void runMessageWithMsgExecuteIndex(IMessage<H> message, String msgExecuteIndex) {
		executorService.getEventLoop(msgExecuteIndex).execute(new Message0(this, message));
	}

	@Override
	public boolean inSelfThread() {
		return this.getExecutor().inSelfThread();
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

	protected static class MessageHandlerEventLoop {
		private final List<DExecutorService> eventLoops;

		public MessageHandlerEventLoop(int count) {
			this.eventLoops = IntStream.range(0, count)
				.mapToObj(DExecutorService::new)
			.toList();
			ShutdownHookUtil.getInstance().addLast(() -> {
				eventLoops.forEach(DExecutorService::shutdown);
			});
		}

		/**
		 * 随机指定一个 DExecutorService
		 * @return
		 */
		public DExecutorService randEventLoop() {
			return eventLoops.get(MathUtil.random(eventLoops.size()));
		}

		public DExecutorService getEventLoop(Object key) {
			int i = Math.abs(Objects.requireNonNull(key).hashCode()) % eventLoops.size();
			return eventLoops.get(i);
		}
	}

	private static class DExecutorService extends ThreadPoolExecutor implements ISyncDbExecutor {
		private final String threadName;
		private Thread thread;

		public DExecutorService(int id) {
			super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
			this.threadName = "message-handler-"+id;
			this.setThreadFactory(this::newThread);
		}

		private Thread newThread(Runnable runnable) {
			this.thread = new FastThreadLocalThread(runnable, this.threadName);
			this.thread.setDaemon(true);
			return this.thread;
		}
		@Override
		public boolean inSelfThread() {
			return this.thread == Thread.currentThread();
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
	private static class Message0 implements Runnable {
		private static final long WARN_NANO_TIME = TimeUnit.MILLISECONDS.toNanos(500);
		private final MessageHandler handler;
		private final IMessage message;
		private final long addDt;

		public Message0(MessageHandler handler, IMessage message) {
			this.addDt = System.nanoTime();
			this.handler = handler;
			this.message = message;
		}

		@Override
		public void run() {
			if (handler.isDestroyed()) {
				handler.logger.error("MessageHandler already destroy! message {} discard!", message.toString());
				return;
			}

			long handlerStart = System.nanoTime();
			if (handlerStart - addDt > WARN_NANO_TIME){
				handler.logger.error("Message {} wait [{}] ms to executor!", message.toString(), TimeUnit.NANOSECONDS.toMillis(handlerStart - addDt));
			}
			try {
				message.execute(handler);
			} catch (Exception e) {
				handler.exceptionHandle(e);
			}finally {
				long handlerEnd = System.nanoTime();
				if (handlerEnd - handlerStart > WARN_NANO_TIME && !IRequestContext.class.isAssignableFrom(message.getClass())){
					handler.logger.error("Message {} use [{}] ms to executor!", message, TimeUnit.NANOSECONDS.toMillis(handlerEnd - handlerStart));
				}
			}
		}
	}
}
