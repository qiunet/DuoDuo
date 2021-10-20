package org.qiunet.flash.handler.context.request.persistconn;

import io.netty.channel.Channel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class PersistConnPbRequestContext<RequestData, P extends IMessageActor<P>>
		extends AbstractPersistConnRequestContext<RequestData, P> {

	public PersistConnPbRequestContext(MessageContent content, Channel channel, P messageActor) {
		super(content, channel, messageActor);
	}

	@Override
	public void execute(P p) {
		try {
			this.handlerRequest();
		}catch (Exception e) {
			DefaultProtobufMessage protobufMessage = channel.attr(ServerConstants.HANDLER_PARAM_KEY).get().getStartupContext().exception(e);
			ChannelUtil.getSession(channel).sendMessage(protobufMessage);
		}
	}

	@Override
	public void handlerRequest() throws Exception{
		if (handler.needAuth() && ! messageActor.isAuth()) {
			ChannelUtil.getSession(channel).close(CloseCause.ERR_REQUEST);
			return;
		}

		FacadePersistConnRequest<RequestData, P> facadeWebSocketRequest = new FacadePersistConnRequest<>(this);
		if (logger.isInfoEnabled() && ! getHandler().getClass().isAnnotationPresent(SkipDebugOut.class)) {
			logger.info("[{}] <<< {}", messageActor.getIdentity(), ToStringBuilder.reflectionToString(getRequestData(), ToStringStyle.SHORT_PREFIX_STYLE));
		}


		((IPersistConnHandler) getHandler()).handler(messageActor, facadeWebSocketRequest);
	}
}
