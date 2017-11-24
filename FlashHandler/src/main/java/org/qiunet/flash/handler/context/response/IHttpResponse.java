package org.qiunet.flash.handler.context.response;
/**
 * Created by qiunet.
 * 17/11/24
 */
public interface IHttpResponse<ResponseData> {
	/***
	 * @param data
	 */
	void response(ResponseData data);
}
