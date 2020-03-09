package org.qiunet.flash.handler.netty.client.trigger;

import com.google.protobuf.InvalidProtocolBufferException;
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
	void response(FullHttpResponse response) throws InvalidProtocolBufferException;
}
