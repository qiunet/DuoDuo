package org.qiunet.cross.transaction;

import com.google.common.base.Preconditions;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.timer.timeout.TimeOutFuture;
import org.qiunet.utils.timer.timeout.Timeout;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

/***
 * transaction 能少用就少用. 因为同步, 可能导致线程池线程消耗光.
 *
 * 发起事务后, 返回的事务句柄.
 * 因为不能cancel. 所以封装一层
 *
 * @author qiunet
 * 2020-10-22 14:52
 */
public class TransactionFuture<T extends ITransactionRsp> {

	private final long id;

	private final DPromise<T> future;

	TransactionFuture(long id, DPromise<T> future) {
		Preconditions.checkNotNull(future);
		this.future = future;
		this.id = id;
		this.future.whenComplete((res, ex) -> this.clear());
	}

	void beginCalTimeOut(int timeout, TimeUnit unit) {
		TimeOutFuture timeOutFuture = Timeout.newTimeOut(f -> {
			future.tryFailure(new CustomException("Transaction Timeout"));
			this.clear();
		}, timeout, unit);
		this.future.whenComplete((res, ex) -> timeOutFuture.cancel());
	}

	void clear() {
		TransactionManager.instance.removeTransaction(id);
	}

	public long getId() {
		return id;
	}

	/***
	 * 默认3秒get  get不到.会抛出 TimeoutException 异常.
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws TimeoutException
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

	/**
	 * 异步. 完成后的通知
	 * @param action
	 */
	public void whenComplete(BiConsumer<? super T, ? super Throwable> action){
		future.whenComplete(action);
	}
}
