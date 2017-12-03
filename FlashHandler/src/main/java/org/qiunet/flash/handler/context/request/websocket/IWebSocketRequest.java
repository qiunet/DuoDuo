package org.qiunet.flash.handler.context.request.websocket;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.flash.handler.context.response.IResponse;


/**
 * Created by qiunet.
 * 17/12/2
 */
public interface IWebSocketRequest<RequestData> extends IRequest<RequestData>, IResponse {

	Channel channel();
}
