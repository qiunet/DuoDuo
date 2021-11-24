package org.qiunet.flash.handler.context.request.http;


/**
 * 异步http请求
 * Created by qiunet.
 * 17/11/21
 */
public interface IAsyncHttpRequest<RequestData, ResponseData> extends IHttpRequest<RequestData> {
	/**
	 * 响应数据
	 */
	void response(ResponseData responseData);
}
