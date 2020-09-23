package org.qiunet.flash.handler.context.request.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.utils.async.LazyLoader;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class WebSocketProtobufRequestContext<RequestData, P extends IPlayerActor> extends AbstractWebSocketRequestContext<RequestData, P> {
	private LazyLoader<RequestData> requestData = new LazyLoader<>(() -> getHandler().parseRequestData(messageContent.bytes()));

	public WebSocketProtobufRequestContext(MessageContent content, ChannelHandlerContext ctx, P playerActor, HttpHeaders headers) {
		super(content, ctx, playerActor, headers);
	}
	@Override
	public RequestData getRequestData() {
		return requestData.get();
	}

	@Override
	public void execute(P p) {
		this.handlerRequest();
	}

	@Override
	public void handlerRequest() {
		FacadeWebSocketRequest<RequestData, P> facadeWebSocketRequest = new FacadeWebSocketRequest<>(this);
		if (logger.isInfoEnabled() && ! getHandler().getClass().isAnnotationPresent(SkipDebugOut.class)) {
			logger.info("[{}] <<< {}", playerActor.getPlayerId(), ToStringBuilder.reflectionToString(getRequestData(), ToStringStyle.SHORT_PREFIX_STYLE));
		}

		try {
			((IWebSocketHandler) getHandler()).handler(playerActor, facadeWebSocketRequest);
		} catch (Exception e) {
			logger.error("WebSocketProtobufRequestContext Exception", e);
		}
	}
}
