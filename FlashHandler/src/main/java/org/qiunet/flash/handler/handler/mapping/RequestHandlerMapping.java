package org.qiunet.flash.handler.handler.mapping;

import com.baidu.bjf.remoting.protobuf.utils.FieldUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.message.UriHttpMessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.utils.collection.DuMap;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 全局的RequestHandler 一个对应Mapping 类
 * 单例模式
 * @author qiunet
 *         Created on 17/3/3 16:46.
 */
public enum RequestHandlerMapping {
	INSTANCE;

	private Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**所有非游戏的 http handler*/
	private Map<String, IHttpHandler> uriPathHandlers = Maps.newHashMap();
	/**所有游戏的 handler*/
	private Map<Integer, IHandler> gameHandlers = Maps.newHashMapWithExpectedSize(128);
	/**req Class 到protocolId 的映射关系**/
	private DuMap<Class<? extends IpbRequestData>, Integer> req2ProtocolId = new DuMap<>(128);


	public static RequestHandlerMapping getInstance() {
		return INSTANCE;
	}

	/**
	 * 通过class得到对应的protocolId
	 * @param clazz
	 * @return
	 */
	public int getProtocolId(Class<? extends IpbRequestData> clazz) {
		if (! req2ProtocolId.containsKey(clazz)) {
			throw new CustomException("class ["+clazz.getName()+"] is not mapping any protocolId!");
		}
		return req2ProtocolId.getVal(clazz);
	}
	/**
	 * 通过请求的MessageContent 得到一个Handler
	 * @param protocolId
	 * @return
	 */
	public IHandler getHandler(int protocolId) {
		if (! gameHandlers.containsKey(protocolId)) {
			logger.error("Have not handler For ProtocolId [{}]", protocolId);
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
			logger.error("Have not handler For UriPath [{}]", uriPath);
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
	void addHandler(int protocolId, IHandler handler) {
		if (this.gameHandlers.containsKey(protocolId)) {
			throw new CustomException("protocolId ["+protocolId+"] className ["+handler.getClass().getSimpleName()+"] is already exist!");
		}

		Class requestDataClass = handlerSetRequestDataClass(handler);
		setHandlerField(handler, "protocolId", protocolId);
		logger.info("ProtocolID [{}] RequestHandler [{}] was found and mapping.", protocolId, handler.getClass().getSimpleName());
		this.gameHandlers.put(protocolId, handler);

		if (requestDataClass != null && IpbRequestData.class.isAssignableFrom(requestDataClass)) {
			int requestId = handler.getClass().getAnnotation(RequestHandler.class).ID();
			if (req2ProtocolId.containsKey(requestDataClass)) {
				throw new CustomException("Already exist class [{}] in mapping, Mapping id is [{}].", requestDataClass.getName(), req2ProtocolId.getVal(requestDataClass));
			}

			if (req2ProtocolId.containsVal(requestId)) {
				throw new CustomException("Already exist requestId [{}] in mapping, Mapping class is [{}]", requestId, req2ProtocolId.getKey(requestId));
			}

			req2ProtocolId.put(requestDataClass, requestId);
		}
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

		handlerSetRequestDataClass(handler);
		logger.info("RequestHandler [{}] uriPath [{}] was found and add.", handler.getClass().getSimpleName(), uriPath);
		this.uriPathHandlers.put(uriPath, handler);
	}

	/***
	 * 给handler 设置 requestDataClass 属性
	 * @param handler
	 */
	private Class handlerSetRequestDataClass(IHandler handler){
		Class oriClazz = handler.getClass();
		Class clazz = oriClazz;
		Class requestDataClass = null;
		do {
			if (! (clazz.getGenericSuperclass() instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
				continue;
			}
			// 可能有的第一位是PlayerActor或者PlayerActor泛型类型 第二位才是requestClass类型.
			Type[] types = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
			Type type = types[0];
			if (type instanceof Class) {
				requestDataClass = (Class) type;
			}
			if (requestDataClass == null || IMessageActor.class.isAssignableFrom(requestDataClass)) {
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
		return requestDataClass;
	}

	/**
	 * 找到 Handler 的requestId field
	 *
	 * @param handler
	 */
	private void setHandlerField(IHandler handler, String fieldName, Object value) {
		Class clazz = handler.getClass();
		Field field = FieldUtils.findField(clazz, fieldName);
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
