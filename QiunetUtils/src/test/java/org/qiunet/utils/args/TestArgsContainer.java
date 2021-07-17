package org.qiunet.utils.args;

import org.junit.Assert;
import org.junit.Test;

/***
 * args 容器测试
 *
 * @author qiunet
 * 2020-08-26 08:20
 **/
public class TestArgsContainer {
	private static final ArgsContainer container = new ArgsContainer();
	@Test
	public void test() throws Exception {
		Argument<Boolean> arg = container.getArgument(ArgumentKeys.TEST_KEY);
		Assert.assertTrue(arg.isEmpty());
		arg.compareAndSet(null, true);
		Assert.assertTrue(arg.get());
	}

	@Test
	public void getArgument() {
		Argument<Boolean> arg = container.getArgument(ArgumentKeys.TEST_KEY, false);
		Assert.assertNull(arg);
	}
}
