package org.qiunet.flash.handler.handler.mapping;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.message.UriHttpMessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.utils.exceptions.SingletonException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局的RequestHandler 一个对应Mapping 类
 * 单例模式
 * @author qiunet
 *         Created on 17/3/3 16:46.
 */
public class RequestHandlerMapping {
	private Logger logger = LoggerType.DUODUO.getLogger();

	private volatile static RequestHandlerMapping instance;
	/**所有游戏的 handler*/
	private Map<Integer, IHandler> gameHandlers = new HashMap<>();
	/**所有非游戏的 http handler*/
	private Map<String, IHttpHandler> uriPathHandlers = new HashMap<>();

	private RequestHandlerMapping() {
		synchronized (RequestHandlerMapping.class) {
			if (instance != null ){
				throw new SingletonException("RequestHandlerMapping instance was duplicate");
			}
			instance = this;
		}
	}

	public static RequestHandlerMapping getInstance() {
		if (instance == null) {
			synchronized (RequestHandlerMapping.class) {
				if (instance == null)
				{
					new RequestHandlerMapping();
				}
			}
		}
		return instance;
	}

	/**
	 * 通过请求的MessageContent 得到一个Handler
	 * @param protocolId
	 * @return
	 */
	public IHandler getHandler(int protocolId) {
		if (! gameHandlers.containsKey(protocolId)) {
			logger.error("Have not handler For ProtocolId ["+protocolId+"]");
		}
		return gameHandlers.get(protocolId);
	}

	/**
	 * 通过请求的MessageContent 得到一个Handler
	 * @param uriPath
	 * @return
	 */
	public IHandler getHandler(String uriPath) {
		if (! uriPathHandlers.containsKey(uriPath)) {
			logger.error("Have not handler For UriPath ["+uriPath+"]");
		}
		return uriPathHandlers.get(uriPath);
	}

	/**
	 * 通过请求的MessageContent 得到一个Handler
	 * @param content
	 * @return
	 */
	public IHandler getHandler(MessageContent content) {
		if (content.getProtocolId() > 0) {
			return getHandler(content.getProtocolId());
		}
		return getHandler(((UriHttpMessageContent) content).getUriPath());
	}
	/**
	 * 存一个handler对应mapping
	 * @param protocolId
	 * @param handler
	 */
	public void addHandler(int protocolId, IHandler handler) {
		if (this.gameHandlers.containsKey(protocolId)) {
			throw new RuntimeException("protocolId ["+protocolId+"] className ["+handler.getClass().getSimpleName()+"] is already exist!");
		}

		handlerSetRequestDataClass(handler);
		setHandlerField(handler, "protocolId", protocolId);
		logger.info("ProtocolID ["+protocolId+"] RequestHandler ["+handler.getClass().getSimpleName()+"] was found and mapping.");
		this.gameHandlers.put(protocolId, handler);
	}


	/**
	 * 存一个handler对应mapping
	 * @param uriPath
	 * @param handler
	 */
	public void addHandler(String uriPath, IHttpHandler handler) {
		if(! uriPath.startsWith("/")) uriPath = "/" + uriPath;

		if (this.uriPathHandlers.containsKey(uriPath)) {
			throw new RuntimeException("uriPath ["+uriPath+"] is already exist!");
		}

		handlerSetRequestDataClass(handler);
		logger.info("RequestHandler ["+handler.getClass().getSimpleName()+"] uriPath ["+uriPath+"] was found and add.");
		this.uriPathHandlers.put(uriPath, handler);
	}

	/***
	 * 给handler 设置 requestDataClass 属性
	 * @param handler
	 */
	private void handlerSetRequestDataClass(IHandler handler){
		Class oriClazz = handler.getClass();
		Class clazz = oriClazz;
		do {
			if (! (clazz.getGenericSuperclass() instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
				continue;
			}
			// 可能有的第一位是PlayerActor或者PlayerActor泛型类型 第二位才是requestClass类型.
			Type[] types = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
			Type type = types[0];
			Class requestDataClass = null;
			if (type instanceof Class) {
				requestDataClass = (Class) type;
			}
			if (requestDataClass == null || IPlayerActor.class.isAssignableFrom(requestDataClass)) {
				if (types.length > 1) {
					requestDataClass = (Class) types[1];
				}else {
					clazz = clazz.getSuperclass();
					continue;
				}
			}
			Preconditions.checkNotNull(requestDataClass, "Handler origin class [%s] current class [%s] get requestClass error", oriClazz.getSimpleName(), clazz.getSimpleName());
			setHandlerField(handler, "requestDataClass", requestDataClass);
			break;
		}while (clazz != Object.class);
	}

	/**
	 * 找到 Handler 的requestId field
	 *
	 * @param handler
	 */
	private void setHandlerField(IHandler handler, String fieldName, Object value) {
		Field field = null;
		Class clazz = handler.getClass();
		do {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
			if (field != null) break;

		}while (clazz != Object.class);

		if (field == null) {
			throw new NullPointerException("not found field name ["+fieldName+"] in handler ["+handler.getClass().getSimpleName()+"]");
		}

		field.setAccessible(true);
		try {
			field.set(handler, value);
		} catch (IllegalAccessException e) {
			logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
		}
	}
}
