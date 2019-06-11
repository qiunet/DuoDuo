package org.qiunet.flash.handler.netty.client.trigger;

import io.netty.handler.codec.http.FullHttpResponse;
import org.qiunet.flash.handler.context.header.IProtocolHeaderAdapter;

/**
 * Created by qiunet.
 * 17/11/29
 */
public interface IHttpResponseTrigger {
	/***
	 *
	 * @param response
	 */
	public void response(IProtocolHeaderAdapter adapter, FullHttpResponse response) ;
}
