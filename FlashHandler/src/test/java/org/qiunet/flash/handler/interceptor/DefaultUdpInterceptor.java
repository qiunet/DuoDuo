package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.UdpInterceptor;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/20 14:51
 **/
public class DefaultUdpInterceptor implements UdpInterceptor {
	@Override
	public void handler(ITcpHandler handler, IUdpRequest request) {

	}
}
