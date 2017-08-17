package org.qiunet.flash.handler.context;

import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.response.IResponse;

/**
 * tcp udp的上下文
 * Created by qiunet.
 * 17/7/19
 */
public class TcpContext<RequestData> extends BaseContext<RequestData> implements ITcpContext<RequestData>,IResponse {
	private String sessionId;
	public TcpContext(IHandler handler, RequestData requestData,String sessionId) {
		super(handler, requestData);
		this.sessionId = sessionId;
	}

	@Override
	public void response(int protocolId, Object o) {

	}
	@Override
	public int getQueueHandlerIndex() {
		return sessionId.hashCode();
	}

	@Override
	public boolean handler() {

		return true;
	}
}
