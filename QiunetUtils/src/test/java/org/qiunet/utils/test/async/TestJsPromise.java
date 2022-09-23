package org.qiunet.utils.test.async;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.async.promise.JsPromise;

import java.util.concurrent.atomic.AtomicReference;

/***
 *
 * @author qiunet
 * 2022/3/14 17:52
 */
public class TestJsPromise {

	@Test
	@Disabled(value = "Do it manual!")
	public void thenAccept() {
		AtomicReference<String> result = new AtomicReference<>();
		new JsPromise<>(() -> "world")
				.then(word -> "hello " + word)
				.accept(result::set);
		Assertions.assertEquals(result.get(), "hello world");
	}


	@Test
	@Disabled(value = "Do it manual!")
	public void thenException() {
		AtomicReference<Throwable> result = new AtomicReference<>();
		new JsPromise<>(() -> "123")
				.then(num -> "hello " + num)
				.then(Integer::parseInt)
				.exception(result::set);

		Assertions.assertNotNull(result.get());
	}

	@Test
	@Disabled(value = "Do it manual!")
	public void thenExceptionally() {
		AtomicReference<Integer> result = new AtomicReference<>();
		new JsPromise<>(() -> "123")
				.then(num -> "hello " + num)
				.then(Integer::parseInt)
				.exceptionally(ex -> 5)
				.accept(result::set);

		Assertions.assertEquals(5, result.get());
	}
}
