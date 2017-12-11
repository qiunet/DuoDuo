package org.qiunet.test.response.annotation.support;

import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.test.response.ILongConnResponse;
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
		return  ILongConnResponse.class.isAssignableFrom(clazz)
				&& ! Modifier.isAbstract(clazz.getModifiers());
	}

	@Override
	public void handler(Class<?> clazz) {
		Response responseAnnotation = clazz.getAnnotation(Response.class);
		if (responseAnnotation == null) throw new NullPointerException("class ["+clazz.getSimpleName()+"] not define ID");
		try {
			Constructor<ILongConnResponse> constructor = (Constructor<ILongConnResponse>) clazz.getConstructor(null);
			if (!constructor.isAccessible()) constructor.setAccessible(true);
			ILongConnResponse response = constructor.newInstance();
			if (response != null) {
				ResponseMapping.getInstance().addResponse(responseAnnotation.ID(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
