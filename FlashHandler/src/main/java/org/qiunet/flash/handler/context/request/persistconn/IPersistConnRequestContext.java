package org.qiunet.flash.handler.context.request.persistconn;

import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.IRequestContext;

/**
 * Created by qiunet.
 * 17/12/2
 */
public interface IPersistConnRequestContext<RequestData, P extends IMessageActor<P>> extends IRequestContext<RequestData>, IPersistConnRequest<RequestData>, IMessage<P> {

}
