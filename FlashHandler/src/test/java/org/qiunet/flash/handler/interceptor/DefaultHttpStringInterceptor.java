package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.HttpInterceptor;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * Created by qiunet.
 * 17/11/23
 */
public class DefaultHttpStringInterceptor implements HttpInterceptor {
	private static final QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

	@Override
	public Object handler(IHttpHandler handler, IHttpRequest request) {

		return "收到";
	}
}
