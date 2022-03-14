package org.qiunet.flash.handler.context.session.future;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;

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
	 * 状态
	 */
	private final AtomicReference<Status> status = new AtomicReference<>(Status.NONE);
	/**
	 * 携带的消息
	 */
	private final IChannelMessage<?> message;
	/**
	 * future
	 */
	private final DefaultChannelPromise channelFuture;

	public DMessageContentFuture(Channel channel, IChannelMessage<?> message) {
		this.channelFuture = new DefaultChannelPromise(channel);
		this.message = message;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return status.compareAndSet(Status.NONE, Status.CANCEL);
	}

	@Override
	public void addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
		this.channelFuture.addListener(listener);
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


	public IChannelMessage<?> getMessage() {
		return message;
	}

	/**
	 * 完成处理
	 * @param future1
	 */
	public <F extends Future<?>> void complete(F future1) {
		future1.addListener(f1 -> {
			if (status.compareAndSet(DMessageContentFuture.Status.NONE, DMessageContentFuture.Status.SUCCESS)) {
				this.channelFuture.trySuccess();
			}
		});
	}

	@Override
	public ChannelFuture future() {
		return this.channelFuture;
	}
}
