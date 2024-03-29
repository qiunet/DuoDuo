package org.qiunet.utils.test.scanner;

import org.junit.jupiter.api.Assertions;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.util.Set;

/**
 * @author qiunet
 *         Created on 17/1/23 19:56.
 */
public class ActionScannerHandler implements IApplicationContextAware {

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) {
		Set<Class<?>> set = context.getTypesAnnotatedWith(HandlerAction.class);
		Assertions.assertEquals(set.size(), 1);
		for (Class<?> s : set) {
			TestClassScanner.clazzName = s.getSimpleName();
		}
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.NONE;
	}
}
