package org.qiunet.flash.handler.common.enums;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.http.HttpProtobufRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpStringRequestContext;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.TcpProtobufRequestContext;
import org.qiunet.flash.handler.context.request.tcp.TcpStringRequestContext;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.context.request.websocket.WebSocketProtobufRequestContext;
import org.qiunet.flash.handler.context.request.websocket.WebSocketStringRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.gzip.GzipUtil;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

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
		public <T> T parseBytes(byte[] bytes, Object... args) {
			return (T) new String(bytes, CharsetUtil.UTF_8);
		}

		@Override
		public <T> T parseBytesGz(byte[] bytes, Object... args) {
			return (T) new String(GzipUtil.validAndDecryptBytes(bytes), CharsetUtil.UTF_8);
//			return (T) new String(GzipUtil.unzip(bytes), CharsetUtil.UTF_8);
		}

		@Override
		public IHttpRequestContext createHttpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpRequest request) {
			return new HttpStringRequestContext(content, channelContext, params, request);
		}

		@Override
		public IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpHeaders headers) {
			return new WebSocketStringRequestContext(content, channelContext, params, headers);
		}

		@Override
		public ITcpRequestContext createTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, TcpBootstrapParams params) {
			return new TcpStringRequestContext(content, channelContext, params);
		}
	},
	/**
	 * protobuf
	 */
	PROTOBUF {
		private ConcurrentHashMap<Class<?>, Parser> class2Parse = new ConcurrentHashMap<>(256);

		@Override
		public <T> T parseBytes(byte[] bytes, Object... args) {
			Parser<T> parser = class2Parse.get(args[0]);
			if (parser == null) {
				try {
					Field field = ((Class) args[0]).getDeclaredField("PARSER");
					field.setAccessible(true);
					parser = (Parser) field.get(null);
					class2Parse.putIfAbsent((Class<?>) args[0], parser);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			try {
				return parser.parseFrom(bytes);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public <T> T parseBytesGz(byte[] bytes, Object... args) {
			return parseBytes(bytes, args);
		}

		@Override
		public IHttpRequestContext createHttpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpRequest request) {
			return new HttpProtobufRequestContext(content, channelContext, params, request);
		}

		@Override
		public IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpHeaders headers) {
			return new WebSocketProtobufRequestContext(content, channelContext, params, headers);
		}

		@Override
		public ITcpRequestContext createTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, TcpBootstrapParams params) {
			return new TcpProtobufRequestContext(content, channelContext, params);
		}
	},
	;

	public abstract <T> T parseBytes(byte[] bytes, Object... args);

	public abstract <T> T parseBytesGz(byte[] bytes, Object... args);

	/**
	 * 得到一个http的context
	 *
	 * @param content
	 * @param channelContext
	 * @param request
	 * @return
	 */
	public abstract IHttpRequestContext createHttpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpRequest request);

	/**
	 * 得到一个webSocket使用的context
	 *
	 * @param content
	 * @param channelContext
	 * @return
	 */
	public abstract IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpHeaders headers);

	/**
	 * 得到一个tcp使用的context
	 *
	 * @param content
	 * @param channelContext de * @return
	 */
	public abstract ITcpRequestContext createTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, TcpBootstrapParams params);
}
