package org.qiunet.flash.handler.handler.mapping;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Modifier;

/**
 * 扫描requestHandler的一个类
 * @author qiunet
 *         Created on 17/3/3 16:42.
 */
public class RequestScannerHandler implements IApplicationContextAware {
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) {
		this.context = context;
		context.getTypesAnnotatedWith(UriPathHandler.class).stream()
			.filter(c -> !Modifier.isAbstract(c.getModifiers()))
			.forEach(this::handler);
	}

	private void handler(Class<?> clazz) {
		UriPathHandler otherRequestHandler = clazz.getAnnotation(UriPathHandler.class);
		IHttpHandler handler = (IHttpHandler) context.getInstanceOfClass(clazz);
		RequestHandlerMapping.getInstance().addHandler(otherRequestHandler.value(), handler);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.ALL;
	}
}
