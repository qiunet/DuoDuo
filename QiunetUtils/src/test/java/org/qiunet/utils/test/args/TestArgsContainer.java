package org.qiunet.utils.test.args;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Assertions.assertTrue(arg.isNull());
		arg.compareAndSet(null, true);
		Assertions.assertTrue(arg.get());

		arg = container.getArgument(ArgumentKeys.TEST_KEY, false);
		Assertions.assertTrue(arg.get());
	}

	@Test
	public void getArgument() {
		ArgsContainer container = new ArgsContainer();
		Argument<Boolean> arg = container.getArgument(ArgumentKeys.TEST_KEY, false);
		Assertions.assertNull(arg);
	}
}
