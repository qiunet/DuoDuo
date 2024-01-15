package org.qiunet.utils.http;

import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 * @author qiunet
 * 2024/1/15 09:49
 ***/
public interface IHttpCallBack {
	/**
	 * 成功的回调
	 * @param response 响应数据
	 */
	void success(HttpResponse response);
	/**
	 * 失败
	 * @param request 请求数据
	 * @param throwable 异常
	 */
	default void fail(HttpRequest request, Throwable throwable){
		LoggerType.DUODUO_HTTP.error(request.toString() + "Exception: ", throwable);
	}
}
