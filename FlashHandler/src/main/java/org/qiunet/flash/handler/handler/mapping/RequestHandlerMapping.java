package org.qiunet.flash.handler.handler.mapping;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Map;

/**
 * 全局的RequestHandler 一个对应Mapping 类
 * 单例模式
 * @author qiunet
 *         Created on 17/3/3 16:46.
 */
public enum RequestHandlerMapping {
	INSTANCE;

	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**所有非游戏的 http handler*/
	private final Map<String, IHttpHandler> uriPathHandlers = Maps.newHashMap();

	public static RequestHandlerMapping getInstance() {
		return INSTANCE;
	}
	/**
	 * 通过请求的MessageContent 得到一个Handler
	 * @param uriPath
	 * @return
	 */
	public IHandler getHandler(String uriPath) {
		if (! uriPathHandlers.containsKey(uriPath)) {
			logger.error("Have not handler For UriPath [{}]", uriPath);
		}
		return uriPathHandlers.get(uriPath);
	}


	/**
	 * 存一个handler对应mapping
	 * @param uriPath
	 * @param handler
	 */
	void addHandler(String uriPath, IHttpHandler handler) {
		if(! uriPath.startsWith("/")) uriPath = "/" + uriPath;

		if (this.uriPathHandlers.containsKey(uriPath)) {
			throw new CustomException("uriPath ["+uriPath+"] is already exist!");
		}
		logger.info("RequestHandler [{}] uriPath [{}] was found and add.", handler.getClass().getSimpleName(), uriPath);
		this.uriPathHandlers.put(uriPath, handler);
	}
}
