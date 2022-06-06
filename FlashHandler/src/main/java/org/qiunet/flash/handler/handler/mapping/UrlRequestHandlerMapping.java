package org.qiunet.flash.handler.handler.mapping;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.slf4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * 扫描requestHandler的一个类
 * @author qiunet
 *         Created on 17/3/3 16:42.
 */
public class UrlRequestHandlerMapping implements IApplicationContextAware {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) {
		this.context = context;
		context.getTypesAnnotatedWith(UriPathHandler.class).stream()
			.filter(c -> !Modifier.isAbstract(c.getModifiers()))
			.forEach(this::addHandler);
	}

	/**所有非游戏的 http handler*/
	private static final Map<String, IHttpHandler> uriPathHandlers = Maps.newHashMap();
	/**
	 * 通过请求的MessageContent 得到一个Handler
	 * @param uriPath
	 * @return
	 */
	public static IHandler getHandler(String uriPath) {
		return uriPathHandlers.get(uriPath);
	}


	/**
	 * 存一个handler对应mapping
	 */
	void addHandler(Class<?> clazz) {
		UriPathHandler otherRequestHandler = clazz.getAnnotation(UriPathHandler.class);
		IHttpHandler handler = (IHttpHandler) context.getInstanceOfClass(clazz);

		String uriPath = otherRequestHandler.value();
		if(! uriPath.startsWith("/")) uriPath = "/" + uriPath;

		if (uriPathHandlers.containsKey(uriPath)) {
			throw new CustomException("uriPath ["+uriPath+"] is already exist!");
		}
		logger.info("RequestHandler [{}] uriPath [{}] was found and add.", handler.getClass().getSimpleName(), uriPath);
		uriPathHandlers.put(uriPath, handler);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.URL_REQUEST;
	}
}
