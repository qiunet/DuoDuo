package org.qiunet.flash.handler.context;

import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.response.IResponse;

/**
 * tcp udp的上下文
 * Created by qiunet.
 * 17/7/19
 */
public class TcpContext<RequestData> extends BaseContext<RequestData> implements IResponse {

	public TcpContext(IHandler handler, RequestData requestData) {
		super(handler, requestData);
	}

	@Override
	public void response(Object o) {

	}
}
