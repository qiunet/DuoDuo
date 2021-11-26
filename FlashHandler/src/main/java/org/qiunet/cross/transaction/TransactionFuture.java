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
 * 发起事务后, 返回的事务句柄.
 * 因为不能cancel. 所以封装一层
 *
 * @author qiunet
 * 2020-10-22 14:52
 */
public class TransactionFuture<T extends BaseTransactionResponse> {

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

	public T get() throws ExecutionException, InterruptedException {
		return future.get();
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
