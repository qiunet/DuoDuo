package org.qiunet.utils.async.promise;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/***
 * 类似js的Promise
 *
 * @author qiunet
 * 2022/3/14 16:54
 */
public class JsPromise<T> {
	private CompletableFuture<T> future;

	private JsPromise(CompletableFuture<T> future) {
		this.future = future;
	}

	public JsPromise(Supplier<T> supplier) {
		this(CompletableFuture.supplyAsync(supplier));
	}

	private JsPromise(JsPromise<?> parent, Function fn) {
		this((CompletableFuture<T>) parent.future.thenApply(fn));
	}


	private JsPromise(JsPromise<?> parent, Consumer action) {
		this((CompletableFuture<T>) parent.future.thenAccept(action));
	}

	/**
	 * 往下执行.
	 * @param fn
	 * @param <R>
	 * @return
	 */
	public <R> JsPromise<R> then(final Function<T, R> fn) {
		 return new JsPromise<>(this, fn);
	}

	public  JsPromise<Void> accept(Consumer<T> action) {
		return new JsPromise<>(this, action);
	}

	public JsPromise<T> exception(Consumer<Throwable> exConsumer) {
		this.future = this.future.whenComplete((res, ex) -> {
			if (ex != null) {
				exConsumer.accept(ex);
			}
		});
		return this;
	}
	/**
	 * 异常.给定一个正常值.
	 * @param exConsumer
	 * @return
	 */
	public JsPromise<T> exceptionally(Function<Throwable, T> exConsumer) {
		this.future = this.future.exceptionally(exConsumer);
		return this;
	}
	/**
	 * 获取最终结果
	 */
	public T get() throws ExecutionException, InterruptedException {
		return this.future.get();
	}
}
