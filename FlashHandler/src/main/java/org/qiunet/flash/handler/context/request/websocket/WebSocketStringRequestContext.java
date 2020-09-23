package org.qiunet.flash.handler.context.request.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.utils.async.LazyLoader;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class WebSocketStringRequestContext<P extends IPlayerActor> extends AbstractWebSocketRequestContext<String, P> {
	private LazyLoader<String> requestData = new LazyLoader<>(() -> getHandler().parseRequestData(messageContent.bytes()));

	public WebSocketStringRequestContext(MessageContent content, ChannelHandlerContext ctx, P playerActor, HttpHeaders headers) {
		super(content, ctx, playerActor, headers);
	}
	@Override
	public String getRequestData() {
		return requestData.get();
	}

	@Override
	public void execute(P p) {
		this.handlerRequest();
	}

	@Override
	public void handlerRequest() {
		FacadeWebSocketRequest<String, P> facadeWebSocketRequest = new FacadeWebSocketRequest<>(this);
		try {
			((IWebSocketHandler) getHandler()).handler(playerActor, facadeWebSocketRequest);
		} catch (Exception e) {
			logger.error("WebSocketStringRequestContext Exception:", e);
		}
	}
}
