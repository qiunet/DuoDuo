package org.qiunet.test.response.annotation.support;

import org.qiunet.test.response.ILongConnResponse;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Modifier;

/**
 * 扫描requestHandler的一个类
 * @author qiunet
 * Created on 17/3/3 16:42.
 */
public class ResponseScannerHandler implements IApplicationContextAware {
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) {
		this.context  = context;

		context.getSubTypesOf(ILongConnResponse.class).stream()
			.filter(c -> ! Modifier.isAbstract(c.getModifiers()))
			.forEach(this::handler);
	}

	private void handler(Class<?> clazz) {
		Response responseAnnotation = clazz.getAnnotation(Response.class);
		if (responseAnnotation == null) throw new NullPointerException("class ["+clazz.getSimpleName()+"] not define ID");
		try {
			ILongConnResponse response = (ILongConnResponse) context.getInstanceOfClass(clazz);
			if (response != null) {
				ResponseMapping.getInstance().addResponse(responseAnnotation.ID(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
