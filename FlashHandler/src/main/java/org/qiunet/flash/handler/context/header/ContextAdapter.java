package org.qiunet.flash.handler.context.header;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/22
 */
public interface ContextAdapter {
	/**
	 * 是否是有效的handler
	 * @param content
	 * @return
	 */
	IHandler getHandler(MessageContent content);
	/**
	 * 得到一个http的context
	 * @param content
	 * @param channelContext
	 * @param request
	 * @return
	 */
	IHttpRequestContext createHttpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params, HttpRequest request);
	/**
	 * 得到一个webSocket使用的context
	 * @param content
	 * @param channelContext
	 * @return
	 */
	IWebSocketRequestContext createWebSocketRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, HttpBootstrapParams params);
	/**
	 * 得到一个tcp使用的context
	 * @param content
	 * @param channelContext
	 * @return
	 */
	ITcpRequestContext createTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext, IHandler handler, TcpBootstrapParams params);
}
