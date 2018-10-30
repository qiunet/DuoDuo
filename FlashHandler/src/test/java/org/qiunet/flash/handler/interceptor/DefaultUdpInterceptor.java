package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.handler.udp.IUdpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.UdpInterceptor;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/20 14:51
 **/
public class DefaultUdpInterceptor implements UdpInterceptor {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	@Override
	public void handler(IUdpHandler handler, IUdpRequest request) {
		try {
			logger.info("protocolId: "+handler.getProtocolID() + "requestData: "+request.getRequestData());
			handler.handler(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
