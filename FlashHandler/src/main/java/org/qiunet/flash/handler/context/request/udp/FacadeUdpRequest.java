package org.qiunet.flash.handler.context.request.udp;

import org.qiunet.flash.handler.netty.server.udp.handler.UdpChannel;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/30 14:52
 **/
class FacadeUdpRequest<RequestData> implements IUdpRequest<RequestData> {
	private AbstractUdpRequestContext<RequestData, Object> context;

	FacadeUdpRequest(AbstractUdpRequestContext<RequestData, Object> context) {
		this.context = context;
	}

	@Override
	public UdpChannel channel() {
		return this.context.channel();
	}

	@Override
	public RequestData getRequestData() {
		return this.context.getRequestData();
	}

	@Override
	public String getRemoteAddress() {
		return this.context.getRemoteAddress();
	}

	@Override
	public Object getAttribute(String key) {
		return this.context.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object val) {
		this.context.setAttribute(key, val);
	}

	@Override
	public void udpResponse(int protocolId, Object o) {
		this.context.udpResponse(protocolId, o);
	}
}
