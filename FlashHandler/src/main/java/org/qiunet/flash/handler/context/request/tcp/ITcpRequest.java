package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.request.IRequest;

/**
 * Created by qiunet.
 * 17/11/21
 */
public interface ITcpRequest<RequestData> extends IRequest<RequestData> {
	/****
	 *
	 * @return
	 */
	Channel channel();
}
