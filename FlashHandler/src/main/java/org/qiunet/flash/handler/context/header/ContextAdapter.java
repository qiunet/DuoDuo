package org.qiunet.flash.handler.context.header;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;

/**
 * Created by qiunet.
 * 17/11/22
 */
public interface ContextAdapter {
	/**
	 * 得到一个http的context
	 * @param content
	 * @param channelContext
	 * @param request
	 * @return
	 */
	IHttpRequestContext createHttpRequestContext(MessageContent content, ChannelHandlerContext channelContext, HttpRequest request);

	/**
	 * 得到一个tcp使用的context
	 * @param content
	 * @param channelContext
	 * @return
	 */
	ITcpRequestContext createTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext);
}
