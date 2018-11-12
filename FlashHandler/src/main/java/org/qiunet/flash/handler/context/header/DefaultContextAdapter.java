package org.qiunet.flash.handler.context.header;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.message.UriHttpMessageContent;
import org.qiunet.flash.handler.context.request.http.HttpProtobufRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpStringRequestContext;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.TcpProtobufRequestContext;
import org.qiunet.flash.handler.context.request.tcp.TcpStringRequestContext;
import org.qiunet.flash.handler.context.request.udp.IUdpRequestContext;
import org.qiunet.flash.handler.context.request.udp.UdpProtobufRequestContext;
import org.qiunet.flash.handler.context.request.udp.UdpStringRequestContext;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.context.request.websocket.WebSocketProtobufRequestContext;
import org.qiunet.flash.handler.context.request.websocket.WebSocketStringRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;
import org.qiunet.flash.handler.netty.server.udp.handler.UdpChannel;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class DefaultContextAdapter implements IContextAdapter {

	@Override
	public IHandler getHandler(MessageContent content) {
		if (content == null) return null;

		if (content.getProtocolId() > 0) {
			return RequestHandlerMapping.getInstance().getGameHandler(content.getProtocolId());
		}else {
			return RequestHandlerMapping.getInstance().getUriPathRequestHandler(((UriHttpMessageContent) content).getUriPath());
		}
	}

	@Override
	public IHttpRequestContext createHttpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpRequest request) {
		switch (handler.getDataType()) {
			case STRING:
				return new HttpStringRequestContext(content, channelContext, params, request);
			case PROTOBUF:
				return new HttpProtobufRequestContext(content, channelContext, params, request);
		}
		return null;
	}

	@Override
	public IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpHeaders headers) {
		switch (handler.getDataType()) {
			case STRING:
				return new WebSocketStringRequestContext(content, channelContext, params, headers);
			case PROTOBUF:
				return new WebSocketProtobufRequestContext(content, channelContext, params, headers);
		}
		return null;
	}

	@Override
	public ITcpRequestContext createTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, TcpBootstrapParams params) {
		switch (handler.getDataType()) {
			case STRING:
				return new TcpStringRequestContext(content, channelContext, params);
			case PROTOBUF:
				return new TcpProtobufRequestContext(content, channelContext, params);
		}
		return null;
	}

	@Override
	public IUdpRequestContext createUdpRequestContext(MessageContent content, UdpChannel udpChannel, IHandler handler, UdpBootstrapParams params) {
		switch (handler.getDataType()) {
			case STRING:
				return new UdpStringRequestContext(content, udpChannel, params);
			case PROTOBUF:
				return new UdpProtobufRequestContext(content, udpChannel, params);
		}
		return null;
	}
}
