package org.qiunet.test.response.annotation.support;

import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.test.response.IResponse;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.utils.classScanner.IScannerHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * 扫描requestHandler的一个类
 * @author qiunet
 *         Created on 17/3/3 16:42.
 */
public class ResponseScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return clazz.getAnnotation(Response.class) != null
				&& ! Modifier.isAbstract(clazz.getModifiers())
				&& IHandler.class.isAssignableFrom(clazz);
	}

	@Override
	public void handler(Class<?> clazz) {
		Response reponseAnnotation = clazz.getAnnotation(Response.class);
		try {
			Constructor<IResponse> constructor = (Constructor<IResponse>) clazz.getConstructor(null);
			if (!constructor.isAccessible()) constructor.setAccessible(true);
			IResponse response = constructor.newInstance();
			if (response != null) {
				ResponseMapping.getInstance().addResponse(reponseAnnotation.ID(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
