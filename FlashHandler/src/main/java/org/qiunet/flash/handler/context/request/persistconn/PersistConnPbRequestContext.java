package org.qiunet.flash.handler.context.request.persistconn;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.string.ToString;

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
			IChannelMessage<IChannelData> protobufMessage = channel.attr(ServerConstants.HANDLER_PARAM_KEY).get().getStartupContext().exception(e);
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
			logger.info("[{}] <<< {}", messageActor.getIdentity(), ToString.toString(getRequestData()));
		}


		((IPersistConnHandler) getHandler()).handler(messageActor, facadeWebSocketRequest);
	}
}
