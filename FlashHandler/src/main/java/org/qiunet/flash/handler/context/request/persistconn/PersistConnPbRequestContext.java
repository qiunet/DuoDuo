package org.qiunet.flash.handler.context.request.persistconn;

import io.netty.channel.Channel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.util.ChannelUtil;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class PersistConnPbRequestContext<RequestData, P extends IMessageActor>
		extends AbstractPersistConnRequestContext<RequestData, P> {

	public PersistConnPbRequestContext(MessageContent content, Channel channel, P messageActor) {
		super(content, channel, messageActor);
	}

	@Override
	public void execute(P p) {
		this.handlerRequest();
	}

	@Override
	public void handlerRequest() {
		if (handler.needAuth() && ! messageActor.isAuth()) {
			ChannelUtil.getSession(channel).close(CloseCause.ERR_REQUEST);
			return;
		}

		FacadePersistConnRequest<RequestData, P> facadeWebSocketRequest = new FacadePersistConnRequest<>(this);
		if (logger.isInfoEnabled() && ! getHandler().getClass().isAnnotationPresent(SkipDebugOut.class)) {
			logger.info("[{}] <<< {}", messageActor.getIdent(), ToStringBuilder.reflectionToString(getRequestData(), ToStringStyle.SHORT_PREFIX_STYLE));
		}

		try {
			((IPersistConnHandler) getHandler()).handler(messageActor, facadeWebSocketRequest);
		} catch (Exception e) {
			logger.error("WebSocketProtobufRequestContext Exception", e);
		}
	}
}
