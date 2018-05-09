package org.qiunet.flash.handler.context.request.websocket;

import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;

/**
 * Created by qiunet.
 * 17/12/2
 */
public interface IWebSocketRequestContext<RequestData> extends IRequestContext<RequestData>, IWebSocketRequest<RequestData> {

}
