package org.qiunet.utils.classScanner;

/**
 * @author qiunet
 *         Created on 17/1/23 19:56.
 */
public class ActionScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return clazz.getAnnotation(HandlerAction.class) != null;
	}
	
	@Override
	public void handler(Class<?> clazz) {
		TestClassScanner.clazzName = clazz.getSimpleName();
	}
}
