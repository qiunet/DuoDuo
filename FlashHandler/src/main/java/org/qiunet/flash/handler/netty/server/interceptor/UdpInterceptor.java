package org.qiunet.flash.handler.netty.server.interceptor;

import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.handler.udp.IUdpHandler;

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
	void handler(IUdpHandler handler, IUdpRequest request);
}
