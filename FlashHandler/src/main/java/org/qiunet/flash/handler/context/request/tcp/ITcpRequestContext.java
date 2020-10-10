package org.qiunet.flash.handler.context.request.tcp;

import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.IRequestContext;

/**
 * Created by qiunet.
 * 17/7/21
 */
public interface ITcpRequestContext<RequestData, P extends IMessageActor> extends IRequestContext<RequestData>, ITcpRequest<RequestData>, IMessage<P> {

}
