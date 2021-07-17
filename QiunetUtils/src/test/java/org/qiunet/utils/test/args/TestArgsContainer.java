package org.qiunet.utils.test.args;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;

/***
 * args 容器测试
 *
 * @author qiunet
 * 2020-08-26 08:20
 **/
public class TestArgsContainer {
	@Test
	public void test() throws Exception {
		ArgsContainer container = new ArgsContainer();
		Argument<Boolean> arg = container.getArgument(ArgumentKeys.TEST_KEY);
		Assert.assertTrue(arg.isEmpty());
		arg.compareAndSet(null, true);
		Assert.assertTrue(arg.get());

		arg = container.getArgument(ArgumentKeys.TEST_KEY, false);
		Assert.assertTrue(arg.get());
	}

	@Test
	public void getArgument() {
		ArgsContainer container = new ArgsContainer();
		Argument<Boolean> arg = container.getArgument(ArgumentKeys.TEST_KEY, false);
		Assert.assertNull(arg);
	}
}
