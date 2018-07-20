package org.qiunet.flash.handler.context.request.udp;

import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.flash.handler.context.response.IResponse;
import org.qiunet.flash.handler.netty.server.udp.handler.UdpChannel;

/***
 *
 * @param <RequestData>
 */
public interface IUdpRequest<RequestData> extends IRequest<RequestData>, IResponse {
	/****
	 *
	 * @return
	 */
	UdpChannel channel();
}
