package org.qiunet.test.response.annotation.support;

import org.qiunet.test.response.ILongConnResponse;
import org.qiunet.test.response.annotation.Response;
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
public class ResponseScannerHandler implements IApplicationContextAware {

	@Override
	public void setApplicationContext(IApplicationContext context) {
		Set<Class<? extends ILongConnResponse>> set = context.getSubTypesOf(ILongConnResponse.class);
		for (Class<? extends ILongConnResponse> clazz : set) {
			if (Modifier.isAbstract(clazz.getModifiers())) continue;

			this.handler(clazz);
		}
	}

	private void handler(Class<?> clazz) {
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
