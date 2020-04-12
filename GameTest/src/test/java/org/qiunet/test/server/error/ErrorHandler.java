package org.qiunet.test.server.error;

import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.netty.server.param.adapter.IClientErrorMessage;

/**
 * Created by qiunet.
 * 17/12/11
 */
public class ErrorHandler implements IClientErrorMessage {
	@Override
	public IResponseMessage getHandlerNotFound() {

		return null;
	}

	@Override
	public IResponseMessage exception(Throwable cause) {
		return null;
	}


}
