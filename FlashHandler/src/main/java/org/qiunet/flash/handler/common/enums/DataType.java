package org.qiunet.flash.handler.common.enums;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.http.HttpProtobufRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpStringRequestContext;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.TcpProtobufRequestContext;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.context.request.websocket.WebSocketProtobufRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

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
		public IHttpRequestContext createHttpRequestContext(MessageContent content, Channel channel, IHandler handler, HttpBootstrapParams params, HttpRequest request) {
			return new HttpStringRequestContext(content, channel, params, request);
		}

		@Override
		public IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor, HttpHeaders headers) {
			throw new IllegalStateException("Not Support");
		}

		@Override
		public ITcpRequestContext createTcpRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor) {
			throw new IllegalStateException("Not Support");
		}
	},
	/**
	 * protobuf
	 */
	PROTOBUF {

		@Override
		public IHttpRequestContext createHttpRequestContext(MessageContent content, Channel channel, IHandler handler, HttpBootstrapParams params, HttpRequest request) {
			return new HttpProtobufRequestContext(content, channel, params, request);
		}

		@Override
		public IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor, HttpHeaders headers) {
			return new WebSocketProtobufRequestContext(content, channel, messageActor, headers);
		}

		@Override
		public ITcpRequestContext createTcpRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor) {
			return new TcpProtobufRequestContext(content, channel, messageActor);
		}
	},
	;


	/**
	 * 得到一个http的context
	 * @param content
	 * @param channel
	 * @param request
	 * @return
	 */
	public abstract IHttpRequestContext createHttpRequestContext(MessageContent content, Channel channel, IHandler handler, HttpBootstrapParams params, HttpRequest request);
	/**
	 * 得到一个webSocket使用的context
	 * @param content
	 * @param channel
	 * @return
	 */
	public abstract IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor, HttpHeaders headers);
	/**
	 * 得到一个tcp使用的context
	 * @param content
	 * @param channel
	de * @return
	 */
	public abstract ITcpRequestContext createTcpRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor);
}
