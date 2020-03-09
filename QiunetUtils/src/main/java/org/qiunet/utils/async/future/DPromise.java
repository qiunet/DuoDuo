package org.qiunet.utils.async.future;

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
}
