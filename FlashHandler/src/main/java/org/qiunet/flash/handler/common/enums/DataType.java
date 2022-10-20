package org.qiunet.flash.handler.common.enums;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.http.HttpJsonRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpPbRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpStringRequestContext;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequestContext;
import org.qiunet.flash.handler.context.request.persistconn.PersistConnPbRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;

/**
 * 传输使用的数据类型
 * Created by qiunet.
 * 17/11/21
 */
public enum DataType {
	/***
	 * 字符串
	 */
	STRING {
		@Override
		public IHttpRequestContext createHttpRequestContext(MessageContent content, Channel channel, IHandler handler, ServerBootStrapParam params, HttpRequest request) {
			return new HttpStringRequestContext(content, channel, params, request);
		}
	},
	/**
	 * protobuf
	 */
	PROTOBUF {

		@Override
		public IHttpRequestContext createHttpRequestContext(MessageContent content, Channel channel, IHandler handler, ServerBootStrapParam params, HttpRequest request) {
			return new HttpPbRequestContext(content, channel, params, request);
		}

		@Override
		public IPersistConnRequestContext createPersistConnRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor) {
			return PersistConnPbRequestContext.valueOf(content, channel, messageActor);
		}
	},
	/**
	 * json
	 */
	JSON {
		@Override
		public IHttpRequestContext createHttpRequestContext(MessageContent content, Channel channel, IHandler handler, ServerBootStrapParam params, HttpRequest request) {
			return new HttpJsonRequestContext(content, channel, params, request);
		}
	}
	;


	/**
	 * 得到一个http的context
	 * @param content
	 * @param channel
	 * @param request
	 * @return
	 */
	public abstract IHttpRequestContext createHttpRequestContext(MessageContent content, Channel channel, IHandler handler, ServerBootStrapParam params, HttpRequest request);
	/**
	 * 得到一个webSocket使用的context
	 * @param content
	 * @param channel
	 * @return
	 */
	public IPersistConnRequestContext createPersistConnRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor) {
		throw new IllegalStateException("Not Support");
	}

}
