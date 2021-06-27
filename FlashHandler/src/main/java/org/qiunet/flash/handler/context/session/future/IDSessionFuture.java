package org.qiunet.flash.handler.context.session.future;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/***
 * session 的一个future
 *
 * qiunet
 * 2021/6/26 15:26
 **/
public interface IDSessionFuture {

	/**
	 * 添加
	 * @param listener
	 */
 void addListener(GenericFutureListener<? extends Future<? super Void>> listener);

	/**
	 * 是否完成
	 * @return
	 */
 boolean isDone();

boolean cancel(boolean mayInterruptIfRunning);

 boolean isSuccess();

 boolean isCanceled();
}
