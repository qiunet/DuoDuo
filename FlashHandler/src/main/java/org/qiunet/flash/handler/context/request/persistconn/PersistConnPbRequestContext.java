package org.qiunet.flash.handler.context.request.persistconn;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.string.ToString;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class PersistConnPbRequestContext<RequestData extends IChannelData, P extends IMessageActor<P>>
		extends AbstractPersistConnRequestContext<RequestData, P> {

	public PersistConnPbRequestContext(MessageContent content, Channel channel, P messageActor) {
		super(content, channel, messageActor);
	}

	@Override
	public void execute(P p) {
		try {
			ChannelDataMapping.requestCheck(channel, getRequestData());
			this.handlerRequest();
		}catch (Exception e) {
			channel.attr(ServerConstants.HANDLER_PARAM_KEY).get().getStartupContext().exception(channel, e);
		}
	}

	@Override
	public void handlerRequest() throws Exception{
		if (handler.needAuth() && ! messageActor.isAuth()) {
			logger.error("handler [{}] need auth. but not auth!", handler.getClass().getSimpleName());
			ChannelUtil.getSession(channel).close(CloseCause.ERR_REQUEST);
			return;
		}

		FacadePersistConnRequest<RequestData, P> facadeWebSocketRequest = new FacadePersistConnRequest<>(this);
		if (logger.isInfoEnabled() && ! getRequestData().getClass().isAnnotationPresent(SkipDebugOut.class)) {
			logger.info("[{}] {} <<< {}", messageActor.getIdentity(), channel().attr(ServerConstants.HANDLER_TYPE_KEY).get(), ToString.toString(getRequestData()));
		}


		((IPersistConnHandler) getHandler()).handler(messageActor, facadeWebSocketRequest);
	}
}
