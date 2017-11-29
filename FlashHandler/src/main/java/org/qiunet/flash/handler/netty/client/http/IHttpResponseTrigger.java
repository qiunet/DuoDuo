package org.qiunet.flash.handler.netty.client.http;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by qiunet.
 * 17/11/29
 */
public interface IHttpResponseTrigger {
	/***
	 *
	 * @param response
	 */
	public void response(FullHttpResponse response) ;
}
