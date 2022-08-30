package org.qiunet.utils.http;


import java.net.http.HttpResponse;

/***
 * 返回对象
 *
 * @author qiunet
 * 2020-04-22 19:48
 ***/
public interface IHttpCallBack<T> {
	/**
	 *
	 * @param response 响应
	 */
	void response(HttpResponse<T> response);
}
