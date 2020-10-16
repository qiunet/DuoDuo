package org.qiunet.utils.scanner;

import org.junit.Assert;
import org.qiunet.utils.args.ArgsContainer;

import java.util.Set;

/**
 * @author qiunet
 *         Created on 17/1/23 19:56.
 */
public class ActionScannerHandler implements IApplicationContextAware {

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) {
		Set<Class<?>> set = context.getTypesAnnotatedWith(HandlerAction.class);
		Assert.assertEquals(set.size(), 1);
		for (Class<?> s : set) {
			TestClassScanner.clazzName = s.getSimpleName();
		}
	}
}
