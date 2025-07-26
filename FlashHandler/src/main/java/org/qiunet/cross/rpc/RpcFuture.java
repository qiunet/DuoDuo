package org.qiunet.cross.rpc;

import com.google.common.base.Preconditions;
import io.netty.util.Timeout;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.timer.timeout.TimeoutUtil;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

/***
 *
 * 发起rpc后, 返回的句柄.
 * 因为不能cancel. 所以封装一层
 *
 * @author qiunet
 * 2020-10-22 14:52
 */
public class RpcFuture<T> {
	private final DPromise<T> future;

	private final int id;

	Method method;

	public RpcFuture() {
		this.future = new DCompletePromise<>();
		this.id = 0;
	}

	RpcFuture(int id, DPromise<T> future) {
		Preconditions.checkNotNull(future);
		this.future = future;
		this.id = id;
		this.future.whenComplete((res, ex) -> this.clear());
	}


	void beginCalTimeOut() {
		this.beginCalTimeOut(5);
	}
	/**
	 * 发起后, 监听超时
	 *
	 * @param timeout 超时时间
	 */
	void beginCalTimeOut(int timeout) {
		Timeout timeOutFuture = TimeoutUtil.newTimeOut(f -> {
			future.tryFailure(new TimeoutException("Rpc Timeout"));
			this.clear();
		}, timeout, TimeUnit.SECONDS);
		this.future.whenComplete((res, ex) -> timeOutFuture.cancel());
	}

	void clear() {
		RpcManager.removeMapping(this.id);
	}

	public int getId() {
		return id;
	}

	/***
	 * 默认3秒get  get不到.会抛出 TimeoutException 异常.
	 * @return 值
	 * @throws ExecutionException 执行异常
	 * @throws InterruptedException 打断异常
	 * @throws TimeoutException 超时异常
	 */
	public T get() throws ExecutionException, InterruptedException, TimeoutException {
		return future.get(3, TimeUnit.SECONDS);
	}

	public T get(long milliseconds) throws InterruptedException, ExecutionException, TimeoutException {
		return future.get(milliseconds, TimeUnit.MILLISECONDS);
	}

	public boolean isDone(){
		return future.isDone();
	}

	public boolean isSuccess(){
		return future.isSuccess();
	}

	DPromise<T> getFuture() {
		return future;
	}

	/**
	 * 异步. 完成后的通知
	 * @param action
	 */
	public void whenComplete(BiConsumer<T, Throwable> action){
		future.whenComplete(action);
	}
}
