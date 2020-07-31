package org.qiunet.flash.handler.context.request.websocket;

import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.request.IRequestContext;

/**
 * Created by qiunet.
 * 17/12/2
 */
public interface IWebSocketRequestContext<RequestData, P extends IPlayerActor> extends IRequestContext<RequestData>, IWebSocketRequest<RequestData>, IMessage<P> {

}
