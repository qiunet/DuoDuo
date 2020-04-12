package org.qiunet.flash.handler.bootstrap.error;

import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.netty.server.param.adapter.IClientErrorMessage;

/**
 * Created by qiunet.
 * 17/11/26
 */
public class DefaultErrorMessage implements IClientErrorMessage{


	@Override
	public IResponseMessage getHandlerNotFound() {
		return null;
	}

	@Override
	public IResponseMessage exception(Throwable cause) {
		return null;
	}
}
