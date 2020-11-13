package org.qiunet.utils.async.future;

import java.util.concurrent.Future;

/***
 *
 * @author qiunet
 * 2020/3/9 08:01
 **/
public interface DPromise<V> extends DFuture<V> {
	/**
	 * 尝试设置成功
	 * 与{@link DPromise#tryFailure(Throwable)} 相斥
	 * @param result
	 * @return
	 */
	boolean trySuccess(V result);

	/***
	 * 尝试设置失败
	 * 与{@link DPromise#trySuccess(Object)} 相斥
	 * @param cause
	 * @return
	 */
	boolean tryFailure(Throwable cause);
	@Override
	DPromise<V> await() throws InterruptedException;
	@Override
	DPromise<V> awaitUninterruptibly();

	/**
	 * 使用指定的future生成.
	 * cancel时候. 会cancel 该future
	 * 生成新的Promise
	 * @return
	 */
	static <V> DPromise<V> create(Future<V> future) {
		return new DCompletePromise<>(future);
	}
	/**
	 * 生成新的Promise
	 * @return
	 */
	static <V> DPromise<V> create() {
		return new DCompletePromise<>();
	}
}
