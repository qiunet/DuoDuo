package org.qiunet.flash.handler.common.annotation.support;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * 扫描requestHandler的一个类
 * @author qiunet
 *         Created on 17/3/3 16:42.
 */
public class RequestScannerHandler implements IApplicationContextAware {

	@Override
	public void setApplicationContext(IApplicationContext context) {
		Set<Class<? extends IHandler>> set = context.getSubTypesOf(IHandler.class);
		for (Class<? extends IHandler> aClass : set) {
			if (Modifier.isAbstract(aClass.getModifiers())) continue;

			this.handler(aClass);
		}
	}

	private void handler(Class<?> clazz) {
		RequestHandler requestHandler = clazz.getAnnotation(RequestHandler.class);
		UriPathHandler otherRequestHandler = clazz.getAnnotation(UriPathHandler.class);
		try {
			Constructor<IHandler> constructor = (Constructor<IHandler>) clazz.getConstructor(null);
			if (!constructor.isAccessible()) constructor.setAccessible(true);
			IHandler handler = constructor.newInstance();
			if (requestHandler != null) {
				RequestHandlerMapping.getInstance().addHandler(requestHandler.ID(), handler);
			}else {
				RequestHandlerMapping.getInstance().addHandler(otherRequestHandler.value(), ((IHttpHandler) handler));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
