package org.qiunet.utils.http;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

/**
 * 对数据的处理
 * @author qiunet
 *
 */
interface HttpRequestHandler<T> {
	/**
	 * get 方法的处理
	 * @param params
	 * @return
	 */
	String getMethodHandler(String url, Map<String, Object> params);
	/**
	 * cookie 的处理
	 * @param cookies
	 * @return
	 */
	String cookieHandler(Map<String, Object> cookies, Charset charset)throws Exception;
	/**
	 *  post 方法的处理
	 * @param params
	 * @return
	 */
	HttpEntity postMethodHandler(Map<String, Object> params);
	/***
	 * 错误的处理 默认打印参数
	 * @param params
	 * @param cookies
	 * @param httpStatus
	 * @param url
	 */
	void errorHandler(Map<String, Object> params, Map<String, Object> cookies, int httpStatus, String url);
	/**
	 * 处理结果
	 * @param response
	 * @return
	 */
	void handlerResult(HttpResponse response, Charset charset);
	/**
	 * 返回结果
	 * @return
	 */
	T returnResult();
}
