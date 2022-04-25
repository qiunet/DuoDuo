package org.qiunet.flash.handler.context.session.future;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/***
 *
 *
 * qiunet
 * 2021/6/27 08:20
 **/
public class DChannelFutureWrapper implements IDSessionFuture {
	/**
	 * 如果已经有session 了. 发出的消息就有该channelFuture
	 */
	private final ChannelFuture channelFuture;

	private static final GenericFutureListener<? extends Future<? super Void>>  listener = f -> {
		if (! f.isSuccess()) {
			f.cause().printStackTrace();
		}
	};

	public DChannelFutureWrapper(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
		this.channelFuture.addListener(listener);
	}

	@Override
	public void addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
		this.channelFuture.addListener(listener);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return this.channelFuture.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isDone() {
		return channelFuture.isDone();
	}

	@Override
	public boolean isSuccess() {
		return channelFuture.isSuccess();
	}

	@Override
	public boolean isCanceled() {
		return channelFuture.isCancelled();
	}

	@Override
	public ChannelFuture future() {
		return this.channelFuture;
	}
}
