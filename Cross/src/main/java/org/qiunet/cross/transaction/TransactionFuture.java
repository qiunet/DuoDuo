package org.qiunet.cross.transaction;

import com.google.common.base.Preconditions;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.timer.timeout.TimeOutFuture;
import org.qiunet.utils.timer.timeout.TimeOutManager;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/***
 * 发起事务后, 返回的事务句柄.
 * 因为不能cancel. 所以封装一层
 *
 * @author qiunet
 * 2020-10-22 14:52
 */
public class TransactionFuture<T extends BaseTransactionResponse> {

	private long id;

	private DPromise<T> future;

	TransactionFuture(long id, DPromise<T> future, int timeout, TimeUnit unit) {
		Preconditions.checkNotNull(future);
		this.future = future;
		this.id = id;

		TimeOutFuture timeOutFuture = TimeOutManager.newTimeOut(f -> {
			future.tryFailure(new CustomException("Transaction Timeout"));
		}, timeout, unit);

		this.future.whenComplete((res, ex) -> {
			timeOutFuture.cancel();
			this.clear();
		});
	}

	private void clear() {
		TransactionManager.instance.removeTransaction(id);
	}

	public long getId() {
		return id;
	}

	public T get() {
		try {
			return future.get();
		} catch (Exception e) {
			throw new CustomException(e, "TransactionFuture.get Exception");
		}
	}

	public T get(long milliseconds){
		try {
			return future.get(milliseconds, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			throw new CustomException(e, "TransactionFuture.get Exception");
		}
	}

	public boolean isDone(){
		return future.isDone();
	}

	/**
	 * 异步. 完成后的通知
	 * @param action
	 */
	public void whenComplete(BiConsumer<? super T, ? super Throwable> action){
		future.whenComplete(action);
	}
}
