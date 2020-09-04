package org.qiunet.scanner.test;

import org.junit.Assert;
import org.qiunet.scanner.IApplicationContext;
import org.qiunet.scanner.IApplicationContextAware;

import java.util.Set;

/**
 * @author qiunet
 *         Created on 17/1/23 19:56.
 */
public class ActionScannerHandler implements IApplicationContextAware {

	@Override
	public void setApplicationContext(IApplicationContext context) {
		Set<Class<?>> set = context.getTypesAnnotatedWith(HandlerAction.class);
		Assert.assertEquals(set.size(), 1);
		for (Class<?> s : set) {
			TestClassScanner.clazzName = s.getSimpleName();
		}
	}
}
