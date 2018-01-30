package org.qiunet.flash.handler.common.annotation.support;

import org.qiunet.flash.handler.common.annotation.UriPathRequestHandler;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.utils.classScanner.IScannerHandler;

import java.lang.reflect.Constructor;

/**
 * 扫描requestHandler的一个类
 * @author qiunet
 *         Created on 17/3/3 16:42.
 */
public class RequestScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return (
				clazz.getAnnotation(RequestHandler.class) != null
			 || clazz.getAnnotation(UriPathRequestHandler.class) != null
				)
				&& IHandler.class.isAssignableFrom(clazz);
	}

	@Override
	public void handler(Class<?> clazz) {
		RequestHandler requestHandler = clazz.getAnnotation(RequestHandler.class);
		UriPathRequestHandler otherRequestHandler = clazz.getAnnotation(UriPathRequestHandler.class);
		try {
			Constructor<IHandler> constructor = (Constructor<IHandler>) clazz.getConstructor(null);
			if (!constructor.isAccessible()) constructor.setAccessible(true);
			IHandler handler = constructor.newInstance();
			if (requestHandler != null) {
				RequestHandlerMapping.getInstance().addHandler(requestHandler.ID(), handler);
			}else {
				RequestHandlerMapping.getInstance().addHandler(otherRequestHandler.uriPath(), ((IHttpHandler) handler));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
