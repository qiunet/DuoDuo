package org.qiunet.utils.test.async;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.async.LazyLoader;

import java.util.function.Supplier;

/***
 *
 * @author qiunet
 * 2020-09-23 22:51
 **/
public class TestLazyLoader {

	private static final Supplier<Integer> s = () -> 10;

	@Test
	public void test(){
		LazyLoader<Integer> lazyLoader = new LazyLoader<>(s);
		Assertions.assertEquals(Integer.valueOf(10), lazyLoader.get());
	}
}
