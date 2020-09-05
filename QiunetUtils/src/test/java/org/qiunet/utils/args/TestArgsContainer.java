package org.qiunet.utils.args;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 * @author qiunet
 * 2020-08-26 08:20
 **/
public class TestArgsContainer {
	private static final ArgsContainer0 container = new ArgsContainer0();
	@Test
	public void test() throws Exception {
		ClassScanner.getInstance().scanner();

		Argument<Boolean> arg = container.getArgument(ArgKey.TEST_KEY);
		Assert.assertTrue(arg.isEmpty());
		arg.compareAndSet(null, true);
		Assert.assertTrue(arg.get());
	}
}
