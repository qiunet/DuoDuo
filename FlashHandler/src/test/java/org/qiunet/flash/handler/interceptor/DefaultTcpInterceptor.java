package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.TcpInterceptor;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.util.logging.Logger;

/**
 * Created by qiunet.
 * 17/11/24
 */
public class DefaultTcpInterceptor implements TcpInterceptor {
	private static final QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	@Override
	public void handler(ITcpHandler handler, ITcpRequest request) {
		logger.info("Protocol Id ["+handler.getProtocolID()+"] received message: "+request.getRequestData());
		handler.handler(request);
	}
}
