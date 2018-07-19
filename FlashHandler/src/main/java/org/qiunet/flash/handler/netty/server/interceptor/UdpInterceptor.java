package org.qiunet.flash.handler.netty.server.interceptor;

import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/18 16:13
 **/
public interface UdpInterceptor {

	/***
	 * 自行处理
	 * @param handler
	 * @param request
	 */
	public void handler(ITcpHandler handler, IUdpRequest request);
}
