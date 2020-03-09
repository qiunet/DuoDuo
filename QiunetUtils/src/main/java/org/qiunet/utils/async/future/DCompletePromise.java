package org.qiunet.utils.async.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/***
 *
 * @author qiunet
 * 2020/3/8 22:01
 **/
public class DCompletePromise<V> extends CompletableFuture<V> implements DFuture<V>, DPromise<V>, CompletionStage<V> {


	@Override
	public boolean isSuccess() {
		return ! isCompletedExceptionally();
	}

	@Override
	public boolean trySuccess(V result) {
		return complete(result);
	}

	@Override
	public boolean tryFailure(Throwable cause) {
		return completeExceptionally(cause);
	}
}
