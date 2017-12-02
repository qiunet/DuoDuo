package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.flash.handler.context.response.IResponse;

/**
 * Created by qiunet.
 * 17/11/21
 */
public interface ITcpRequest<RequestData> extends IRequest<RequestData>, IResponse {
	/****
	 *
	 * @return
	 */
	Channel channel();
}
