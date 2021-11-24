package org.qiunet.flash.handler.context.session.future;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/***
 * 还没有连接成功时候, 发送的消息返回的就是该future
 *
 * qiunet
 * 2021/6/27 08:26
 **/
public class DMessageContentFuture implements IDSessionFuture {
	public enum Status {
		NONE, CANCEL, SUCCESS
	}
	/**
	 * 监听
	 */
	private List<GenericFutureListener<? extends Future<? super Void>>> listeners;
	/**
	 * 状态
	 */
	private final AtomicReference<Status> status = new AtomicReference<>(Status.NONE);
	/**
	 * 携带的消息
	 */
	private final IChannelMessage<?> message;

	public DMessageContentFuture(IChannelMessage<?> message) {
		this.message = message;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return status.compareAndSet(Status.NONE, Status.CANCEL);
	}

	@Override
	public void addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
		if (listeners == null) {
			this.listeners = Lists.newArrayListWithExpectedSize(2);
		}
		this.listeners.add(listener);
	}

	@Override
	public boolean isDone() {
		return status.get() != Status.NONE;
	}

	@Override
	public boolean isSuccess() {
		return status.get() == Status.SUCCESS;
	}

	@Override
	public boolean isCanceled() {
		return status.get() == Status.CANCEL;
	}

	private List<GenericFutureListener> getListeners() {
		if (listeners == null) {
			return Collections.emptyList();
		}
		return ImmutableList.copyOf(listeners);
	}

	public IChannelMessage<?> getMessage() {
		return message;
	}

	/**
	 * 完成处理
	 * @param future1
	 */
	public <F extends Future<?>> void complete(F future1) {
		List<GenericFutureListener> listeners = this.getListeners();
		future1.addListener(f1 -> {
			if (status.compareAndSet(DMessageContentFuture.Status.NONE, DMessageContentFuture.Status.SUCCESS)) {
				listeners.forEach(a -> {
					try {
						a.operationComplete(f1);
					} catch (Exception e) {
						LoggerType.DUODUO_FLASH_HANDLER.error("Connect complete exception! ", e);
					}
				});
			}
		});
	}
}
