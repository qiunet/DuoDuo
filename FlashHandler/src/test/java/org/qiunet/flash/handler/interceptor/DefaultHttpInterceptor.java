package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.HttpInterceptor;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/11/23
 */
public class DefaultHttpInterceptor implements HttpInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	@Override
	public Object handler(IHttpHandler handler, IHttpRequest request) {
		if (handler.getDataType() == DataType.STRING) {
			logger.info("收到string请求 message: "+request.getRequestData());
		}else {
			logger.info("收到protobuf请求 message: "+request.getRequestData());
		}
		return handler.handler(request);
	}
}
