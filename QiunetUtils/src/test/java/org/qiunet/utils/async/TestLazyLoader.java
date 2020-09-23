package org.qiunet.utils.async;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals(Integer.valueOf(10), lazyLoader.get());
	}
}
