package org.qiunet.flash.handler.context.request.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultStringMessage;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class WebSocketStringRequestContext extends AbstractWebSocketRequestContext<String, String> {
	private String requestData;
	public WebSocketStringRequestContext(MessageContent content, ChannelHandlerContext ctx, HttpBootstrapParams params, HttpHeaders headers) {
		super(content, ctx, params, headers);
	}
	@Override
	public String getRequestData() {
		if (requestData != null) return requestData;
		this.requestData = getHandler().parseRequestData(messageContent.bytes());
		return requestData;
	}

	@Override
	protected IResponseMessage getResponseMessage(int protocolId, String s) {
		return new DefaultStringMessage(protocolId, s);
	}

	@Override
	public void handlerRequest() {
		FacadeWebSocketRequest<String> facadeWebSocketRequest = new FacadeWebSocketRequest(this);
		params.getWebSocketInterceptor().handler((IWebSocketHandler) getHandler(), facadeWebSocketRequest);
	}
}
