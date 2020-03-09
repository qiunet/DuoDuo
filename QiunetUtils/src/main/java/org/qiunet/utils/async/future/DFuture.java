package org.qiunet.utils.async.future;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

/***
 * 自定义的Future
 * @author qiunet
 * 2020/3/8 11:15
 **/
public interface DFuture<V> extends Future<V> {
	/**
	 * 实在executor线程调用.
	 * 这里是可以引起线程安全, 需要业务自己处理
	 * @param action
	 * @param <> 第一个参数 返回的结果对象
	 * @param <> 第一个参数 可能抛出的异常
	 */
	CompletionStage<V> whenComplete
	(BiConsumer<? super V, ? super Throwable> action);

	/**
	 * 是否成功.
	 * @return
	 */
	boolean isSuccess();
}
