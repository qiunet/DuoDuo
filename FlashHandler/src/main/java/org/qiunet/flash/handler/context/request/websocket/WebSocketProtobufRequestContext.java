package org.qiunet.flash.handler.context.request.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class WebSocketProtobufRequestContext<RequestData, P extends IPlayerActor> extends AbstractWebSocketRequestContext<RequestData, P> {
	private RequestData requestData;
	public WebSocketProtobufRequestContext(MessageContent content, ChannelHandlerContext ctx, P playerActor, HttpHeaders headers) {
		super(content, ctx, playerActor, headers);
	}
	@Override
	public RequestData getRequestData() {
		if (requestData != null) return requestData;
		this.requestData = getHandler().parseRequestData(messageContent.bytes());
		return requestData;
	}

	@Override
	public void execute(P p) {
		this.handlerRequest();
	}

	@Override
	public void handlerRequest() {
		FacadeWebSocketRequest<RequestData, P> facadeWebSocketRequest = new FacadeWebSocketRequest<>(this);
		try {
			((IWebSocketHandler) getHandler()).handler(playerActor, facadeWebSocketRequest);
		} catch (Exception e) {
			logger.error("WebSocketProtobufRequestContext Exception", e);
		}
	}
}
