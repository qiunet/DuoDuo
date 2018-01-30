package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.Channel;

/**
 * tcp request 的外观类
 * Created by qiunet.
 * 17/11/21
 */
class FacadeTcpRequest<RequestData> implements ITcpRequest<RequestData> {
	private AbstractTcpRequestContext<RequestData, Object> context;

	public FacadeTcpRequest (AbstractTcpRequestContext context) {
		this.context = context;
	}
	@Override
	public RequestData getRequestData() {
		return context.getRequestData();
	}

	@Override
	public String getRemoteAddress() {
		return context.getRemoteAddress();
	}

	@Override
	public Object getAttribute(String key) {
		return context.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object val) {
		context.setAttribute(key, val);
	}

	@Override
	public void response(int protocolId, Object responseData) {
		context.response(protocolId, responseData);
	}

	@Override
	public Channel channel() {
		return context.channel();
	}
}
